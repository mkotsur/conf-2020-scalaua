package conf2020scalaua.common

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object ProfilingUtils {

//  def time[R](block: => R): (R, Long, String) = {
//    val t0 = System.nanoTime()
//    val result = block // call-by-name
//    val t1 = System.nanoTime()
//    (result, (t1 - t0), "Elapsed time: " + (t1 - t0) + "ns")
//  }

  def time[R](block: => Future[R]): (R, Long) = {
    val t0 = System.currentTimeMillis()
    val result = Await.result(block, Duration.Inf)
    val t1 = System.currentTimeMillis()
    (result, t1 - t0)
  }

  def report(label: String)(res: (_, Long)): Unit = res match {
    case (_, timeMs: Long) =>
      println(s"Elapsed time [$label]: ${timeMs.toFloat / 1000}s")
  }

}
