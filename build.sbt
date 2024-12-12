import Dependencies._

ThisBuild / scalaVersion     := "2.13.12"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "olx-advertisements-scraper",
    libraryDependencies += munit % Test
  )


libraryDependencies ++= Seq(
  "com.discord4j" % "discord4j-core" % "3.2.7",
  "net.ruippeixotog" %% "scala-scraper" % "3.1.2",
  "org.scalatest" %% "scalatest" % "3.3.0-SNAP4" % Test,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
  "ch.qos.logback" % "logback-classic" % "1.3.5",
  "ch.qos.logback" % "logback-core" % "1.3.5",
  "com.typesafe" % "config" % "1.4.3"
)

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
