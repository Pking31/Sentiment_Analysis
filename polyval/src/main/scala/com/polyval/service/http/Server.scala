package com.polyval.service.http

import com.polyval.domain.AppConfig

import cats.effect.*
import cats.syntax.all.*
import fs2.Stream
import fs2.io.file.Files
import fs2.io.net.Network
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server as Http4sServer
import org.typelevel.log4cats.Logger

object Server {
  def run[F[_]: Async: Network: Logger: Files](appConfig: AppConfig): F[Unit] = {
    val routes = Routes.make[F](appConfig)

    val server: Resource[F, Http4sServer] = EmberServerBuilder.default
      .withHost(appConfig.http.host)
      .withPort(appConfig.http.port)
      .withHttpApp(routes.httpApp)
      .build

    Stream
      .resource(server)
      .evalMap { server =>
        Logger[F].info(s"Server started on ${server.address}") >> Async[F].never[Unit]
      }
      .compile
      .drain
  }
}
