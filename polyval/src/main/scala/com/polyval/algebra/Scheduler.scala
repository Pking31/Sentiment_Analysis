package com.polyval.algebra

import scala.concurrent.duration.*

import com.polyval.domain.Schedule
import com.polyval.domain.Schedule.{FixedRate, Once}

import cats.effect.kernel.Temporal
import cats.effect.std.Supervisor
import cats.syntax.all.*

sealed trait Scheduler[F[_]] {

  /** Schedule the execution of a task at some point in the future.
    *
    * @param fa
    *   task to execute
    * @param delay
    *   delay before execution
    * @param recurrence
    *   recurrence of the task
    * @return
    *   a task that will complete with either an error or a unit value
    */
  def schedule[A](fa: F[A], delay: FiniteDuration, recurrence: Schedule): F[Either[Throwable, Unit]]

}

object Scheduler {

  def apply[F[_]](implicit ev: Scheduler[F]): Scheduler[F] = ev

  def forSupervisorTemporal[F[_]](implicit S: Supervisor[F], T: Temporal[F]): Scheduler[F] =
    new Scheduler[F] {
      def schedule[A](fa: F[A], duration: FiniteDuration, recurrence: Schedule): F[Either[Throwable, Unit]] =
        S.supervise(T.sleep(duration) *> fa).attempt.flatMap {
          case Left(e)  => T.pure(Left(e))
          case Right(_) =>
            recurrence match {
              case Once              => T.pure(Right(()))
              case FixedRate(period) =>
                T.sleep(period) *> schedule(fa, 0.seconds, recurrence)
            }
        }
    }

}
