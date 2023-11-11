package com.polyval.service.http.public

import com.polyval.algebra.CSVSerde
import com.polyval.domain.{AppConfig, RawTweet}

import cats.MonadThrow
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

final case class StateRoute[F[_]: MonadThrow](serde: CSVSerde[F], csv: AppConfig.CsvConfig) extends Http4sDsl[F] {
  private val prefix = "state"

  private val http = HttpRoutes.of[F] {
    case GET -> Root / "all"      =>
      Ok(serde.read[RawTweet](csv.path))
    case GET -> Root / IntVar(id) =>
      Ok(id)
  }

  def routes: HttpRoutes[F] = Router[F](prefix -> http)
}
