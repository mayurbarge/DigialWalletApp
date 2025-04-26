ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.5"
libraryDependencies += "dev.zio" %% "zio" % "2.1.17"
lazy val root = (project in file("."))
  .settings(
    name := "DigitalWalletApp"
  )
