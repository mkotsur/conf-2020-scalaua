package conf2020scalaua

import cats.effect.{IO, Resource}
import conf2020scalaua.common.ProfilingUtils.{report, time}

import scala.concurrent.{ExecutionContext, Future}

object SimpleFuturesApp extends App {

  val pool1 = java.util.concurrent.Executors.newFixedThreadPool(2)
  val fixedEC = ExecutionContext.fromExecutor(pool1)

  val startTestFuture = (ec: ExecutionContext) =>
    Future {
      Thread.sleep(100) // Blocking operation
    }(ec)

  report("One blocking op, fixed=2")(time {
    startTestFuture(fixedEC)
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
    startNTestFutures(10)(fixedEC)
  })

  pool1.shutdown()

  val pool2 = java.util.concurrent.Executors.newCachedThreadPool()

  // This is the moment, where a typo can cost you a lot of debugging time,
  // if you confuse pool2 with pool1
  val cachedEC = ExecutionContext.fromExecutor(pool2)

  report("10 blocking ops, cached")(time {
    startNTestFutures(10)(cachedEC)
  })

  pool2.shutdown()

  // So, from now on, even here, let's treat pool as a resource

  val cachedECRes = Resource
    .make(IO(java.util.concurrent.Executors.newCachedThreadPool()))(
      tp => IO(tp.shutdown())
    )
    .evalMap(executor => IO(ExecutionContext.fromExecutor(executor)))

  cachedECRes.use { implicit ec =>
    report("10 blocking ops, cached, res")(time {
      startNTestFutures(10)
    })
    IO.unit
  }
}
