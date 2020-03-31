package conf2020scalaua

/**
  * Let's imagine this is a Java library, which
  * you ~want~ chose to use in your shine FP Scala project...
  */
object KindOfJavaLib {

  object Jackpot

  def javaGamble: Jackpot.type = {
    Thread.sleep(1000)
    if (System.currentTimeMillis() % 2 == 0) {
      Jackpot
    } else {
      throw new RuntimeException("Sorry, no jackpot this time :-(")
    }
  }
}
