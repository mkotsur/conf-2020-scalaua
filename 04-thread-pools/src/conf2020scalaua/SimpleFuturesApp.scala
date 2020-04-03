package conf2020scalaua

import java.util.concurrent.Executors

import cats.effect.{IO, Resource}
import conf2020scalaua.common.ProfilingUtils.{report, time}

import scala.concurrent.{ExecutionContext, Future}

object SimpleFuturesApp extends App {

  val pool1 = Executors.newFixedThreadPool(2)
  val ec1 = ExecutionContext.fromExecutor(pool1)

  val startTestFuture = (ec: ExecutionContext) =>
    Future {
      Thread.sleep(100) // Blocking operation
    }(ec)

  report("One blocking op, fixed=2")(time {
    startTestFuture(ec1)
  })

  def startNTestFutures(
    n: Int
  )(implicit ec: ExecutionContext): Future[List[Unit]] = {
    // 1. It's notoriously difficult to pass the EC explicitly here!
    // 2. Implicit has two semantical changes here:
    //    - outer: ec can be passed as implicit argument
    //    - inner: ec is implicitly available inside this function
    val ff: Seq[Future[Unit]] =
      Range(1, n).map(_ => startTestFuture(ec))
    Future.sequence(ff.toList)
  }

  report("10 blocking ops, fixed=2")(time {
    startNTestFutures(10)(ec1)
  })

  pool1.shutdown()

  val pool2 = Executors.newCachedThreadPool()

  // This is the moment, where a typo can cost you a lot of debugging time,
  // if you confuse pool2 with pool1
  val cachedEC = ExecutionContext.fromExecutor(pool2)

  report("10 blocking ops, cached")(time {
    startNTestFutures(10)(cachedEC)
  })

  pool2.shutdown()

  // So, from now on, even here, let's treat pool as a resource

  val ec = Resource
    .make(acquire = IO(Executors.newCachedThreadPool()))(
      release = tp => IO(tp.shutdown())
    )
    .evalMap(executor => IO(ExecutionContext.fromExecutor(executor)))

  // You can't use it nor here
  ec.use { implicit ec =>
    // ... only here
    IO(Future(42))
  }
  // ... neither here

  ec.use { implicit ec =>
    report("10 blocking ops, cached, res")(time {
      startNTestFutures(10)
    })
    IO.unit
  }
}
