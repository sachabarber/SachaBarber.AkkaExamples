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
  name := "RemoteCreation",
  version := "1.0",
  scalaVersion := "2.11.8",
  resolvers := allResolvers,
  libraryDependencies := AllLibraryDependencies
)

lazy val demoApp = Project (
  "RemoteCreation-App",
  file ("RemoteCreation-App"),
  settings = commonSettings
)
//build these projects when main App project gets built
  //.aggregate(common)
  //.dependsOn(common)

lazy val common = Project (
  "common",
  file ("RemoteCreation-Common"),
  settings = commonSettings
)

lazy val remote = Project (
  "remote",
  file ("RemoteCreation-Remote"),
  settings = commonSettings
)