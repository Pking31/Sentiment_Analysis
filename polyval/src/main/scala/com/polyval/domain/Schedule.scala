package com.polyval.domain

import scala.concurrent.duration.FiniteDuration

sealed trait Schedule {
  def period: FiniteDuration
}

object Schedule {
  case object Once                                   extends Schedule {
    def period: FiniteDuration = FiniteDuration(0, "ms")
  }
  final case class FixedRate(period: FiniteDuration) extends Schedule
}
