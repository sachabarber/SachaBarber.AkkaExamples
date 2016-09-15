import akka.actor.{ActorSystem, Props}

import scala.io.StdIn


object LocalDemo extends App {

  implicit val system = ActorSystem("LocalDemoSystem")

  //val localActorUsingIdentity = system.actorOf(Props[LocalActorUsingIdentity], name = "LocalActorUsingIdentity")
  val localActorUsingPlainSelection = system.actorOf(Props[LocalActorUsingPlainSelection], name = "LocalActorUsingPlainSelection")


  localActorUsingPlainSelection ! Init

  StdIn.readLine()
  system.terminate()
  StdIn.readLine()

}