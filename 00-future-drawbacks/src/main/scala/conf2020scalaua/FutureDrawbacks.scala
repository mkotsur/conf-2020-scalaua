package conf2020scalaua

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

// Credits to:
//   - https://gist.github.com/enzief/dbf3c0e72ef03860878b77203f62ce87
object FutureDrawbacks extends App {

  import ExecutionContext.Implicits.global

  val f0: Future[Int] = Future { ??? }
  def times2(i: Int): Int = ???

  // Which both:
  // * shows a natural of execution

  // Map
  f0.map(times2)

  // Attach callback
  f0.onComplete {
    case Failure(exception) => ???
    case Success(value)     => ???
  }

  // Convert error
  f0.recover {
    case e: Throwable => ???
  }

  // Await
  Await.ready(f0, Duration.Inf)

  // Do other things

  // * protects the value while it's not available yet!
  //   because
  //   times2(f0) doesn't compile

  // Unfortunately, though, the "instant start" feature
  // is not very good :-| because:
  // * difficult reasoning
  // * difficult refactoring

  // Parallel
  val f: Future[Int] = Future { 1 }
  val g: Future[Int] = Future { 2 }
  for {
    i <- f
    j <- g
  } yield i + j

  // (2)
  for {
    i <- Future { 1 }
    j <- Future { 2 }
  } yield i + j

  // If you extract prepreForA into variable, it will change the sequence of execution

  def prepareForA(): Future[Unit] = ???

  def doA(): Future[Int] = ???

  def doB(): Future[Int] = ???

//  val preparedForA = prepareForA()
  for {
    _ <- doB()
    _ <- prepareForA()
    a <- doA()
  } yield a

  // And in general, you can't have an "orchestration" layer of your program
  // with high-level functions:

  type Name = String
  type Price = Int
  type Importance = Int
  type Activity[T] = (Name, Price, Importance, Future[T])

  def doCheapFirst[T](activities: List[Activity[T]]): Future[T] = ???
  def doImportantFirst[T](activities: List[Activity[T]]): Future[T] = ???
  // Because it doesn't matter what you have in those function, the order will be
  // defined before you write them.

  // So, it's kind of natural to want a future, which doesn't start-off so eagerly.

  // It's good that we can do flatMap, which
  // protects the value from being used too early.
  f0.flatMap(_ => ???)

  // But why expose things like:
  Await.result(f0, Duration.Inf) // THROWS AN EXCEPTION!!!

  //  And throwing exceptions is a big NO-NO, because
  //  it creates problems just like with eager evaluation.

  // GOTO 01
}
