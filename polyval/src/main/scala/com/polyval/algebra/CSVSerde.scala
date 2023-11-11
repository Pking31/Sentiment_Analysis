package com.polyval.algebra

import java.io.InputStreamReader

import scala.jdk.CollectionConverters.*

import com.polyval.syntax.RowDecoder

import cats.effect.kernel.Async
import cats.syntax.applicativeError.*
import fs2.Stream
import fs2.io.file.{Files, Path}
import org.apache.commons.csv.CSVFormat
import org.typelevel.log4cats.Logger

sealed trait CSVSerde[F[_]] {
  def read[A: RowDecoder](path: String): Stream[F, Option[A]]
}

object CSVSerde {

  def make[F[_]: Files: Async: Logger]: CSVSerde[F] = new CSVSerde[F] {
    def read[A: RowDecoder](path: String): Stream[F, Option[A]] =
      for {
        path   <- Stream.eval(Async[F].delay(getClass.getResource(path)))
        input  <- Files[F]
                    .readAll(Path(path.getPath))
                    .onError { e =>
                      Stream.eval(Logger[F].error(e)(s"Error reading file $path"))
                    }
                    .through(fs2.io.toInputStream)
                    .map(new InputStreamReader(_))
        header  = Option(CSVFormat.DEFAULT.parse(input).iterator().next()).map(_.iterator().asScala.toSeq)
        format  =
          CSVFormat.DEFAULT.builder().setHeader(header.getOrElse(Seq.empty): _*).setSkipHeaderRecord(true).build()
        parser <- Stream.eval {
                    Async[F].delay(format.parse(input)).onError { e =>
                      Logger[F].error(e)(s"Error reading workbook")
                    }
                  }
        row    <- Stream.fromIterator[F](parser.iterator().asScala, 1024)
      } yield RowDecoder[A].decode(row)
  }
}
