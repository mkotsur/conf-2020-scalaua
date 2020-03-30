package conf2020scalaua.nf

import scala.concurrent.{ExecutionContext, Future}

/**
  * NF = Next Future
  */
object NF {
  def apply[T](f: => Future[T]): NF[T] = new NF(f)

  def async[T](f: => T)(implicit ec: ExecutionContext): NF[T] =
    NF(Future { f })

  def sync[T](f: => T): NF[T] = NF(Future.successful(f))
}

final class NF[T] private (thunk: => Future[T]) {

  private[nf] def asFuture: Future[T] = thunk

  def flatMap[N](mapper: T => NF[N])(implicit ec: ExecutionContext): NF[N] =
    NF[N](thunk.flatMap(s => mapper(s).asFuture))

  def map[N](mapper: T => N)(implicit ec: ExecutionContext): NF[N] =
    NF[N](thunk.map(mapper))

}
