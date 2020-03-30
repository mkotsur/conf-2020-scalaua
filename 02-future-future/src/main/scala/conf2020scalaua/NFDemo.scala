package conf2020scalaua

import conf2020scalaua.nf.NFApp.ReturnCode
import conf2020scalaua.nf.{NF, NFApp}

import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn

object NFDemo extends NFApp {
  override def run(args: List[String]): NF[ReturnCode] = {
    import ExecutionContext.Implicits.global

    val nameUF = NF.sync(StdIn.readLine())

    for {
      _ <- NF.sync(println(s"What is your name?"))
      name <- nameUF
      _ <- NF.sync(println(s"Hello, $name"))
    } yield 0
  }
}
