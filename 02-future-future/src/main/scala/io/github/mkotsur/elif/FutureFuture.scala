package io.github.mkotsur.elif

import cats.effect.{ExitCode, IO}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.StdIn

object UF {

  // Why a dedicated type?
  // * to find it easier,
  // * to change it easier.
  type UF[T] = () => Future[T]
  type ReturnCode = Int

  def fromFuture[T](f: Future[T]): UF[T] = () => f

  def apply[T](exp: => T)(implicit ec: ExecutionContext): UF[T] =
    fromFuture(Future(exp))

  def flatMap[T, N](obj: UF[T], flatMap: T => UF[N])(
    implicit ec: ExecutionContext
  ): UF[N] = { () =>
    obj().flatMap(s => flatMap(s)())
  }

}

trait UFApp extends scala.AnyRef {

  import UF._

  def run(args: List[String]): UF[ReturnCode]

  def main(args: Array[String]): Unit = {

    val theRun: UF[ReturnCode] = this.run(args.toList)

    val futureResult: Future[ReturnCode] = theRun()

    val code = Await.result(futureResult, Duration.Inf)

    System.exit(code)

  }
}

object FutureFuture extends UFApp {

  import UF._

  override def run(args: List[String]): UF[ReturnCode] = {

    import ExecutionContext.Implicits.global

    IO.apply()

//    val nameFuture = Future { StdIn.readLine() }

//    v1
//    fromFuture(for {
//      name <- fromFuture(nameFuture)()
//      _ <- fromFuture(Future({ println(s"Hello, $name") }))()
//    } yield ())

//    v2: with apply
    val nameUF: () => Future[String] = UF(StdIn.readLine())
//    fromFuture(for {
//      name <- nameUF
//      _ <- UF({ println(s"Hello, $name") })(global)()
//    } yield 0)
//    v3: actually, fromFuture doesn't make any sense,
//    because future is already started
//    val greet = () => {
//      nameUF().flatMap(s => Future(println("Hello, s")))
//    }

//    Let's make it even better by adding a flat map to UF

    flatMap(
      nameUF,
      (name: String) =>
        UF({
          println(s"Hello, $name")
          0
        })
    )

//    Further things: improve syntax, etc
//    but the rest is clear

  }
}
