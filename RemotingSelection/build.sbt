import sbt._
import sbt.Keys._


lazy val allResolvers = Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
)

lazy val AllLibraryDependencies =
  Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.4.8",
    "com.typesafe.akka" %% "akka-remote" % "2.4.8"
  )


lazy val commonSettings = Seq(
  name := "AkkaRemoting",
  version := "1.0",
  scalaVersion := "2.11.8",
  resolvers := allResolvers,
  libraryDependencies := AllLibraryDependencies
)


lazy val remote = (project in file("remote")).
  settings(commonSettings: _*).
  settings(
    // other settings
  )

lazy val local = (project in file("local")).
  settings(commonSettings: _*).
  settings(
    // other settings
  )



