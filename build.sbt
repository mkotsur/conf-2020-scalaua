ThisBuild / name := "conf-scala-ua-2020"

ThisBuild / version := "0.1"

ThisBuild / scalaVersion := "2.13.1"

ThisBuild / libraryDependencies += "org.typelevel" %% "cats-effect" % "2.1.2"

// Sub-projects
lazy val `future-drawbacks` = project in file("00-future-drawbacks")
lazy val `future-future` = project in file("02-future-future")

lazy val `effect-systems` = project in file("01-effect-systems")

lazy val `thread-pools` = (project in file("04-thread-pools"))
  .settings(
    Compile / scalaSource := baseDirectory.value / "src",
    Compile / resourceDirectory := baseDirectory.value / "res"
  )
  .dependsOn(common)

lazy val `refactoring` = (project in file("05-refactoring"))
  .settings(
    Compile / scalaSource := baseDirectory.value / "src",
    Compile / resourceDirectory := baseDirectory.value / "res"
  )
  .dependsOn(common)

// Common project with profiling utils, etc

lazy val common = (project in file("common")).settings(
  Compile / scalaSource := baseDirectory.value / "src",
  Compile / resourceDirectory := baseDirectory.value / "res"
)
