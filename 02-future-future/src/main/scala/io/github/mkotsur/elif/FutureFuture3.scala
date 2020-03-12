package io.github.mkotsur.elif

import cats.effect.{ExitCode, IO, IOApp}
import io.github.mkotsur.elif.FutureFuture2.UF2
import io.github.mkotsur.elif.FutureFuture2.UF2.ReturnCode

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.StdIn

object FutureFuture3 extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    import ExecutionContext.Implicits.global
    val nameUF = UF2.sync(StdIn.readLine())

    IO.fromFuture(IO(Future.successful(12)))

    IO(println("Hello, sir"))

//
//    nameUF
//      .flatMap(name => UF2.sync(println(s"Hello, $name")))
//      .flatMap(_ => UF2.sync(0))

    for {
      _ <- UF2.sync(println(s"What is your name?"))
      name <- nameUF
      _ <- UF2.sync(println(s"Hello, $name"))
    } yield 0

    // Byt we want more power! That's why IO is a much powerful deal.

  }
}
