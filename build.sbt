ThisBuild / name := "conf-scala-ua-2020"

ThisBuild / version := "0.1"

ThisBuild / scalaVersion := "2.13.1"

ThisBuild / libraryDependencies += "org.typelevel" %% "cats-effect" % "2.1.2"
ThisBuild / libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.12.3"
ThisBuild / libraryDependencies += "com.github.pureconfig" %% "pureconfig-cats-effect" % "0.12.3"

lazy val SimplePaths = Array(
  Compile / scalaSource := baseDirectory.value / "src",
  Compile / resourceDirectory := baseDirectory.value / "res"
)

// Sub-projects
lazy val `future-drawbacks` = project in file("00-future-drawbacks")
lazy val `future-future` = project in file("02-future-future")
lazy val `evolutionary-development` =
  (project in file("03-evo-devo"))
    .settings(SimplePaths: _*)

lazy val `effect-systems` =
  (project in file("01-effect-systems"))
    .settings(SimplePaths: _*)

lazy val `thread-pools` = (project in file("04-thread-pools"))
  .settings(SimplePaths: _*)
  .dependsOn(common)

lazy val `refactoring` = (project in file("05-refactoring"))
  .settings(SimplePaths: _*)
  .dependsOn(common)

lazy val `resource` = (project in file("08-resource"))
  .settings(SimplePaths: _*)
  .dependsOn(common)

// Common project with profiling utils, etc

lazy val common = (project in file("common")).settings(SimplePaths: _*)
