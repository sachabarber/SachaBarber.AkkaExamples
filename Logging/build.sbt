name := "HelloWorld"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.11" % "2.4.8",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "com.typesafe.akka" % "akka-slf4j_2.11" % "2.4.8")