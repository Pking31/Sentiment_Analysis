package com.polyval.domain

import cats.ApplicativeThrow
import com.comcast.ip4s.{Host, Port}
import pureconfig.error.CannotConvert
import pureconfig.{ConfigReader, ConfigSource}

import AppConfig.*

final case class AppConfig(
    http: HttpConfig,
    csv: CsvConfig
)

object AppConfig {
  final case class HttpConfig(
      host: Host,
      port: Port
  )

  object HttpConfig {
    implicit val PortReader: ConfigReader[Port] =
      pureconfig.ConfigReader[Int].emap { port =>
        Port.fromInt(port).toRight(CannotConvert(s"$port", "com.comcast.ip4s.Port", "Invalid port"))
      }

    implicit val HostReader: ConfigReader[Host] =
      pureconfig.ConfigReader[String].emap { host =>
        Host.fromString(host).toRight(CannotConvert(s"$host", "com.comcast.ip4s.Host", "Invalid host"))
      }

    implicit val ConfigReader: ConfigReader[HttpConfig] =
      pureconfig.ConfigReader.forProduct2("host", "port")(HttpConfig.apply)
  }

  final case class CsvConfig(
      path: String,
      name: String
  )

  import pureconfig.generic.auto.*

  def load[F[_]: ApplicativeThrow]: F[AppConfig] = ApplicativeThrow[F].catchNonFatal {
    ConfigSource.default.loadOrThrow[AppConfig]
  }
}
