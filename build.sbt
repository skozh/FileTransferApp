
ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "3.2.0"

lazy val root = (project in file("."))
  .settings {
    name := "FileTrnsfrApp"
  }
scalaVersion := "3.2.0"
Compile / mainClass := Some("FileTrnsfr")
libraryDependencies ++= Seq(
      "com.hierynomus" % "sshj" % "0.34.0",
      "log4j" % "log4j" % "1.2.17",
      "org.slf4j" % "slf4j-api" % "2.0.0",
      "org.slf4j" % "slf4j-simple" % "2.0.0",
)
  