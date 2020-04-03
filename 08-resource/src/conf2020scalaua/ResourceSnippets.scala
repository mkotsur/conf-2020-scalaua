package conf2020scalaua

import java.nio.file.{Files, Path}

import cats.effect.{IO, Resource}

object ResourceSnippets {

  //  Resource is a data structure that captures the
  //  effectful allocation of a resource (acquire),
  //  along with its finalizer (release).

  abstract class Resource[F[_], A] {
    def use[B](f: A => F[B]): F[B]
  }
}
