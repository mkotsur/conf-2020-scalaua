package conf2020scalaua

import cats.effect.{ExitCode, IO, IOApp}

object IODemo extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    IO.pure(ExitCode.Success)
  }

}
