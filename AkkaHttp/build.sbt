import sbt._
import sbt.Keys._


lazy val allResolvers = Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
)


lazy val AllLibraryDependencies =
  Seq(
    "com.typesafe.akka" % "akka-actor_2.11" % "2.4.12",
    "com.typesafe.akka" % "akka-http_2.11" % "3.0.0-RC1",
    "com.typesafe.akka" % "akka-http-core_2.11" % "3.0.0-RC1",
    "com.typesafe.akka" % "akka-http-spray-json_2.11" % "3.0.0-RC1"
  )


lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.11.8",
  resolvers := allResolvers,
  libraryDependencies := AllLibraryDependencies
)


lazy val serverside =(project in file("serverside")).
  settings(commonSettings: _*).
  settings(
    name := "serverside"
  )
  .aggregate(common, clientside)
  .dependsOn(common, clientside)

lazy val common = (project in file("common")).
  settings(commonSettings: _*).
  settings(
    name := "common"
  )

lazy val clientside = (project in file("clientside")).
  settings(commonSettings: _*).
  settings(
    name := "clientside"
  )
  .aggregate(common)
  .dependsOn(common)
