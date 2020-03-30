package conf2020scalaua

import java.util.concurrent.ExecutorService

import scala.concurrent.ExecutionContextExecutorService

object SlideSnippets {
  /**
   * Creates an `ExecutionContext` from the given `ExecutorService`
   * with the default reporter.
   */
  trait ExecutionContext {
    def fromExecutorService(e: ExecutorService): ExecutionContextExecutorService = ???
  }
}
