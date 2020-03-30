package conf2020scalaua

object Refactoring {

  type ??? = Nothing

  type CanDo[_] = ???

  abstract class ServiceFoo[C: CanDo] {}

  object WhyYouNeedHigherKindedTypes {

    // From: https://typelevel.org/blog/2016/08/21/hkts-moving-forward.html

    def tuple[A, B](as: List[A], bs: List[B]): List[(A, B)] =
      as.flatMap { a =>
        bs.map((a, _))
      }

    def tuple[A, B](as: Option[A], bs: Option[B]): Option[(A, B)] =
      as.flatMap { a =>
        bs.map((a, _))
      }

    def tuple[E, A, B](as: Either[E, A], bs: Either[E, B]): Either[E, (A, B)] =
      as.flatMap { a =>
        bs.map((a, _))
      }

    // What if we extract "sameness"
    def tuplef[F[_], A, B](fa: F[A], fb: F[B]): F[(A, B)] = ???

  }

}
