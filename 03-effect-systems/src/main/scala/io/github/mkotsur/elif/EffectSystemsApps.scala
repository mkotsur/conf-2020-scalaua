package io.github.mkotsur.elif

import cats.effect.{ExitCode, IO, IOApp}

import scala.concurrent.{ExecutionContext, Future}

object EffectSystemsApps {

  object CatsEffectApp extends IOApp {
    override def run(args: List[String]): IO[ExitCode] = {
      IO.pure(ExitCode.Success)
    }
  }

}
