package conf2020scalaua

import cats.effect.{ExitCode, IO, IOApp}
import conf2020scalaua.KindOfJavaLib.{Jackpot, javaGamble}

object SimpleGambleApp extends IOApp {

  private def gamble: IO[Jackpot.type] = IO(javaGamble)

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- IO(println("Started"))
      jackpot <- gamble
      _ <- IO(println(jackpot))
    } yield ExitCode.Success
  }

}
