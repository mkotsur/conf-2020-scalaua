package conf2020scalaua.nf

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object NFApp {
  type ReturnCode = Int
}

trait NFApp {
  import NFApp._

  // Please override this method!
  def run(args: List[String]): NF[ReturnCode]

  final def main(args: Array[String]): Unit = {
    val resultFuture = this.run(args.toList).asFuture
    val code = Await.result(resultFuture, Duration.Inf)
    System.exit(code)
  }
}
