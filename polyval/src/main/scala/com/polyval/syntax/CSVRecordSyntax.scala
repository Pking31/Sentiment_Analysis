package com.polyval.syntax

import java.time.ZonedDateTime

import scala.util.Try

import org.apache.commons.csv.CSVRecord

object row extends CSVRecordSyntax

trait CSVRecordSyntax {

  final implicit class CSVRecordOps(self: CSVRecord) {

    def fromInt(cell: Int): Option[Int] = Try(self.get(cell).toInt).toOption

    def fromDouble(cell: Int): Option[Double] = Try(self.get(cell).toDouble).toOption

    def fromString(cell: Int): Option[String] = Try(self.get(cell)).toOption

    def fromBoolean(cell: Int): Option[Boolean] = Try(self.get(cell).toBoolean).toOption

    def fromLong(cell: Int): Option[Long] = Try(self.get(cell).toLong).toOption

    def fromTimestamp(cell: Int): Option[ZonedDateTime] =
      Try(self.get(cell)).toOption.flatMap(s => Try(ZonedDateTime.parse(s)).toOption)

    def fromOption[A](ev: RowDecoder[A]): Option[Option[A]] = ev.decode(self).map(Option(_))

  }
}
