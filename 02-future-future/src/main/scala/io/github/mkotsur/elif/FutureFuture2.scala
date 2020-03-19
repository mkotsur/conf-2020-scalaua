package io.github.mkotsur.elif

import io.github.mkotsur.elif.FutureFuture2.UF2
import io.github.mkotsur.elif.FutureFuture2.UF2.ReturnCode

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.StdIn

trait UF2App extends scala.AnyRef {

  def run(args: List[String]): UF2[ReturnCode]

  final def main(args: Array[String]): Unit = {

    val theRun: UF2[ReturnCode] = this.run(args.toList)

    val futureResult: Future[ReturnCode] = theRun()

    val code = Await.result(futureResult, Duration.Inf)

    System.exit(code)
  }
}

object FutureFuture2 extends UF2App {

  import UF2._

  object UF2 {

    type ReturnCode = Int

    def apply[T](f: => Future[T]): UF2[T] = new UF2(f)

    def async[T](f: => T)(implicit ec: ExecutionContext): UF2[T] =
      UF2(Future { f })

    def sync[T](f: => T): UF2[T] = UF2(Future.successful(f))
  }

  final class UF2[T](thunk: => Future[T]) {

    def apply(): Future[T] = thunk

    def flatMap[N](mapper: T => UF2[N])(implicit ec: ExecutionContext): UF2[N] =
      UF2[N](thunk.flatMap(s => mapper(s)()))

    def map[N](mapper: T => N)(implicit ec: ExecutionContext): UF2[N] =
      UF2[N](thunk.map(mapper))
  }

  override def run(args: List[String]): UF2[ReturnCode] = {

    import ExecutionContext.Implicits.global
    val nameUF = UF2.sync(StdIn.readLine())

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
    // How about some trampolining?

    def fib(n: Int, a: Long = 0, b: Long = 1): UF2[Long] =
      IO.suspend {
        if (n == 0) IO.pure(a)
        else {
          val next = fib(n - 1, b, a + b)
          // Every 100 cycles, introduce a logical thread fork
          if (n % 100 == 0)
            cs.shift *> next
          else
            next
        }
      }
  }
}
