import sbt._
import sbt.Keys._


lazy val allResolvers = Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
)

lazy val AllLibraryDependencies =
  Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.4.8",
    "com.typesafe.akka" %% "akka-remote" % "2.4.8",
    "com.typesafe.akka" %% "akka-cluster" % "2.4.8"
  )


lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.11.8",
  resolvers := allResolvers,
  libraryDependencies := AllLibraryDependencies
)


lazy val root =(project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "Base"
  )
  .aggregate(common, frontend, backend)
  .dependsOn(common, frontend, backend)

lazy val common = (project in file("common")).
  settings(commonSettings: _*).
  settings(
    name := "common"
  )

lazy val frontend = (project in file("frontend")).
  settings(commonSettings: _*).
  settings(
    name := "frontend"
  )
  .aggregate(common)
  .dependsOn(common)

lazy val backend = (project in file("backend")).
  settings(commonSettings: _*).
  settings(
    name := "backend"
  )
  .aggregate(common)
  .dependsOn(common)