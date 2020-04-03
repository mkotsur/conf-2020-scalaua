package conf2020scalaua

import cats.effect.{ExitCode, IO, IOApp}
import conf2020scalaua.common.ProfilingUtils

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object ThreadPoolsApps extends App {

  object CatsEffectApp extends IOApp {
    override def run(args: List[String]): IO[ExitCode] = {

      val io = IO {
        Thread.sleep(1000)
        42
      }

      import cats.implicits._
      List.fill(10)(io).parSequence >>
        IO.pure(ExitCode.Success)
    }
  }

}
