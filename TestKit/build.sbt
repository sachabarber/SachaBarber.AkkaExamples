name := "HelloWorld"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka"   %%    "akka-actor"    %   "2.4.8",
  "com.typesafe.akka"   %%    "akka-testkit"  %   "2.4.8",
  "org.scalatest"       %%    "scalatest"     %   "2.2.5"   %   "test"
)

