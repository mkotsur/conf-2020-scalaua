package conf2020scalaua

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

object WhyFutureApp extends App {

  trait MyUserService {
    def getUserName(uid: String): String
  }

  val service1: MyUserService = ???

  val name01: String = service1.getUserName("u01")

//  Is `getUserName` a pure function? ☝ No!

//  Math function:
  // - relation between sets that;
  // - associates to every element of a first set;
  // - exactly one element of the second set.

//  CS pure function:
  //  - return value is the same for the same arguments;
  //  - its evaluation has no side effects.

  sealed trait Shape
  case object Circle extends Shape
  case object Square extends Shape
  case object Rectangle extends Shape
  case object Hexagon extends Shape

  sealed trait Color
  case object Red extends Color
  case object Yellow extends Color
  case object Green extends Color
  case object Purple extends Color
  case object Blue extends Color

  def shapeColour(shape: Shape): Color = ???

// CS function (operation or expression) side effect:
  // Has an observable effect besides the main effect,
  // which is returning the value to the invoker of the function (operation or expression).

// What pure functions ~give~ promise you?
//  - Easier to reason about
//  - Easier to combine
//  - Easier to test
//  - Easier to debug
//  - Easier to parallelize

  // Object Oriented programming simpler?
  // https://www.oodesign.com/

  // Let's check what happens if: user doesn't exist

  service1.getUserName("i-don't-exist") // throws an exception?

  // Solving this problem with an Option

  trait OptionalUserService {

    // User may not exist
    def getUserName(uid: String): Option[String]
  }

  val service2: OptionalUserService = ???

  val nameOrNot: Option[String] =
    service2.getUserName("i-don't-exist")

  //  Practical value:
  nameOrNot.get // is the only way to screw up and not handle the 'non-happy' flow.
  // Should be forbidden. Nonetheless, it's not the problem of `getUserName`

  // Let's check what happens if: network failure?
  val name2: Option[String] = service2.getUserName("u02")
  // We can't return... so we throw

  trait OptionalFailsafeUserService {
    // + there may be an error
    def getUserName(uid: String): Try[Option[String]]
  }

  val service3: OptionalFailsafeUserService = ???

  val name3: Try[Option[String]] = service3.getUserName("u02")
  // Practical value: to extract the name, you'll need to handle unhappy-scenarios
  // unless you screw it up by doing:
  name3.get.get

  // Finally, what if we want to make use of more CPU-cores, we want
  // some way of supporting more than one flow of computation
  // with exchanging results. And that's Future.

  trait TheFinalUserService {

    // There may be an error
    // + it may not exist
    // + we don't know when it's done
    def getUserName(uid: String): Future[Option[String]]
  }

//  And luckily for us, Future combines the 'error-proneness' with the 'separate-flow' nature
// of what we want to do:

  val service4: TheFinalUserService = ???

  private val name4: Future[Option[String]] = service4.getUserName("u05")

  name4
    .onComplete {
      case Success(userOption) => ???
      case Failure(userOption) => ???
    }(ExecutionContext.global)

  // Unless you screw it up with
  name4.value.get.get.get

  // Or with
  var name = ""
  while (name4.value.isEmpty) {
    Thread.sleep(1000)
  }
  name = name4.value.get.get.get
  // So, all and all, Future is quite useful. It leverages
  // Try and Option to ensure you know you need to handle the bad stuff
  // when you want to extract the value
  // or you can just stay in the future, map the and ignore the problems for now.

  name4.map(_.map(name => name.toUpperCase()))(ExecutionContext.global)

  // And the fact that we need to pass the execution context again is interesting...
}
