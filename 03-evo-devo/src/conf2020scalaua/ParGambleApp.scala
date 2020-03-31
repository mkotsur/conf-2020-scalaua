package conf2020scalaua

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import conf2020scalaua.KindOfJavaLib.{Jackpot, javaGamble}

object ParGambleApp extends IOApp {

  private def gamble: IO[Jackpot.type] = IO(javaGamble)

  def gambleGentle: IO[Option[Jackpot.type]] =
    gamble.attempt.map {
      case Right(x) => x.some
      case Left(_)  => None
    }

  private def parGamble: IO[scala.Option[Jackpot.type]] =
    Range(0, 10).map(_ => gambleGentle).toList.parSequence.flatMap {
      _.count(_.isDefined) match {
        case 0 => None.pure[IO]
        case _ => Jackpot.some.pure[IO]
      }
    }

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- IO(println("Started"))
      jackpot <- parGamble
      _ <- IO(println(jackpot))
    } yield ExitCode.Success
  }

}
