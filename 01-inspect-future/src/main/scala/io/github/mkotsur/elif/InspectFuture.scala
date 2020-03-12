package io.github.mkotsur.elif

import cats.effect.{ExitCode, IO, IOApp}

import scala.concurrent.{ExecutionContext, Future}

object InspectFuture extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {

    implicit val executionContext: ExecutionContext = ???

    val f = Future {
      // Code
      // that
      // runs
      // in Thread 1
    }

    // Code
    // that
    // runs
    // in Thread 2

//    f.map()

    IO(ExitCode.Success)
  }
}
