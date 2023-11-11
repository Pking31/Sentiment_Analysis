package com.polyval

import com.polyval.domain.AppConfig
import com.polyval.service.http.Server

import cats.effect.*
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp.Simple {
  implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  def run: IO[Unit] = for {
    appConfig <- AppConfig.load[IO]
    _         <- Server.run[IO](appConfig)
  } yield ()
}
