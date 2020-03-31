package conf2020scalaua

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import conf2020scalaua.KindOfJavaLib.{Jackpot, javaGamble}

object GambleUntilWinApp extends IOApp {

  private def gamble: IO[Jackpot.type] = IO(javaGamble)

  def gambleGentle: IO[Option[Jackpot.type]] =
    gamble.attempt.map {
      case Right(x) => x.some
      case Left(_)  => None
    }

  private def gambleUntilWin(attempts: Int = 100): IO[Jackpot.type] =
    gambleGentle.flatMap {
      case Some(x) =>
        x.pure[IO]
      case None if attempts > 0 =>
        IO(println(s"Retrying. ${attempts - 1} attempts left")) *>
          gambleUntilWin(attempts - 1)
      case None => IO.raiseError(new RuntimeException("Out of attempts"))
    }

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- IO(println("Started"))
      jackpot <- gambleGentle
      _ <- IO(println(jackpot))
    } yield ExitCode.Success
  }

}
