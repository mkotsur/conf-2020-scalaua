package conf2020scalaua

import java.util.concurrent.ExecutorService

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

object SlideSnippets {

  /**
    * Creates an `ExecutionContext` from the given `ExecutorService`
    * with the default reporter.
    */
  trait ExecutionContext {
    def fromExecutorService(
      e: ExecutorService
    ): ExecutionContextExecutorService = ???
  }
  //  An execution context that is safe to use for blocking operations.

  //  final class Blocker private (val blockingContext: ExecutionContext) extends AnyVal
}
