package conf2020scalaua

import cats.effect.{ContextShift, ExitCode, IO, IOApp}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Left, Right, Success}

object IOSnippets {

  implicit val ec: ContextShift[IO] = ???

//    We don't need this to create an IO!
//    import ExecutionContext.Implicits.global

//  Creating IO for values
  IO.pure(42) // IO[Int];
  IO.unit

// Creating IO for computations
  IO.apply(20 + 22) // IO[Int];  Same as IO.delay()
  IO.fromFuture(IO(Future.successful(42))) // IO[Int]

// Creating IO for async computations
//    def async[A](k: (Either[Throwable, A] => Unit) => Unit): IO[A]

  def futureToIO[A](f: Future[A])(implicit ec: ExecutionContext): IO[A] =
    f.value match {
      // Future on complete
      case Some(result) =>
        result match {
          case Success(a) => IO.pure(a)
          case Failure(e) => IO.raiseError(e)
        }
      case _ =>
        IO.async { cb =>
          f.onComplete { r =>
            cb(r match {
              case Success(a) => Right(a)
              case Failure(e) => Left(e)
            })
          }
        }
    }

// Never say never

  // Computation which never completes
  IO.never

// Suspend
  //  def suspend[A](thunk: => IO[A]): IO[A]
  IO.suspend(???)

// For more info see:
//    https://typelevel.org/cats-effect/datatypes/io.html

}
