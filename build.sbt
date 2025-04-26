ThisBuild / version := "0.1.0-SNAPSHOT"

//ThisBuild / scalaVersion := "3.3.5"
ThisBuild / scalaVersion := "2.13.16"
testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")

lazy val root = (project in file("."))
  .settings(
    name := "DigitalWalletApp",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.1.17",
      "dev.zio" %% "zio-prelude" % "1.0.0-RC39",
      "io.jvm.uuid" %% "scala-uuid" % "0.3.1"
    ),
    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.2.19",
      "org.scalatest" %% "scalatest" % "3.2.19" % "test",
      "dev.zio" %% "zio-test"          % "2.1.17" % Test,
      "dev.zio" %% "zio-test-sbt"      % "2.1.17" % Test,
      "dev.zio" %% "zio-test-magnolia" % "2.1.17" % Test
    ),
    resolvers += "Artima Maven Repository" at "https://repo.artima.com/releases"
  )
