package com.polyval.service.http

import scala.concurrent.duration.*

import com.polyval.algebra.CSVSerde
import com.polyval.domain.AppConfig
import com.polyval.service.http.public.StateRoute

import cats.effect.*
import fs2.io.file.Files
import org.http4s.server.Router
import org.http4s.server.middleware.*
import org.http4s.{HttpApp, HttpRoutes}
import org.typelevel.log4cats.Logger

object Routes {
  def make[F[_]: Async: Files: Logger](appConfig: AppConfig): Routes[F] = new Routes[F](appConfig) {}
}

sealed abstract case class Routes[F[_]: Async: Files: Logger](appConfig: AppConfig) {
  private val version = "v1"

  private val routes: HttpRoutes[F] = Router[F](version -> StateRoute[F](CSVSerde.make[F], appConfig.csv).routes)

  private val middleware: HttpRoutes[F] => HttpRoutes[F] = { (routes: HttpRoutes[F]) =>
    AutoSlash(routes)
  } andThen { (routes: HttpRoutes[F]) =>
    Timeout(60.seconds)(routes)
  }

  private val loggers: HttpApp[F] => HttpApp[F] = { (routes: HttpApp[F]) =>
    RequestLogger.httpApp(logHeaders = true, logBody = true)(routes)
  }

  val httpApp: HttpApp[F] = loggers(middleware(routes).orNotFound)
}
