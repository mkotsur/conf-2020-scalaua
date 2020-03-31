package conf2020scalaua

import cats.effect.{ExitCode, IO, IOApp}
import conf2020scalaua.KindOfJavaLib.{Jackpot, javaGamble}
import cats.implicits._

object GentleGambleApp extends IOApp {

  private def gamble: IO[Jackpot.type] = IO(javaGamble)

  def gambleGentle: IO[Option[Jackpot.type]] =
    gamble.attempt.map {
      case Right(x) => x.some
      case Left(_)  => None
    }

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- IO(println("Started"))
      jackpot <- gambleGentle
      _ <- IO(println(jackpot))
    } yield ExitCode.Success
  }

}
