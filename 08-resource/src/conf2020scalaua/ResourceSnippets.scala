package conf2020scalaua

import java.nio.file.{Files, Path}
import java.util.concurrent.Executors

import cats.effect.{ContextShift, IO, Resource}
import conf2020scalaua.common.ProfilingUtils.{report, time}

import scala.concurrent.{ExecutionContext, Future}

object ResourceSnippets {

  //  Resource is a data structure that captures the
  //  effectful allocation of a resource (acquire),
  //  along with its finalizer (release).

  abstract class Resource[F[_], A] {
    def use[B](f: A => F[B]): F[B]
  }

  val ec = Resource
    .make(acquire = IO(Executors.newCachedThreadPool()))(
      release = tp => IO(tp.shutdown())
    )
    .evalMap(executor => IO(ExecutionContext.fromExecutor(executor)))
    .map(ec => ContextShift(ec))

  // You can't use it nor here
  ec.use { implicit ec =>
    // ... only here
    IO(Future(42))
  }
  // ... neither here

  import cats.implicits._

  ec.use { implicit ec =>
    List.fill(10)(IO(42)).parSequence
  }
}
