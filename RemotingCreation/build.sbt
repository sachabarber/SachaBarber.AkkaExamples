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
  .aggregate(common, remote)
  .dependsOn(common, remote)

lazy val common = (project in file("common")).
  settings(commonSettings: _*).
  settings(
    name := "common"
  )

lazy val remote = (project in file("remote")).
  settings(commonSettings: _*).
  settings(
    name := "remote"
  )
  .aggregate(common)
  .dependsOn(common)