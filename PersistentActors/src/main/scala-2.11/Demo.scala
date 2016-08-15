import akka.actor._
import scala.language.postfixOps
import scala.io.StdIn

object Demo extends App {

  //create the actor system
  val system = ActorSystem("PersitenceSystem")

  val persistentActor =
    system.actorOf(Props(classOf[DemoPersistentActor]),
      "demo-persistent-actor-1")

  persistentActor ! "print"
//  persistentActor ! Cmd("foo")
//  persistentActor ! Cmd("baz")
//  persistentActor ! "boom"
//  persistentActor ! Cmd("bar")
//  persistentActor ! "snap"
//  persistentActor ! Cmd("buzz")
//  persistentActor ! "print"


  StdIn.readLine()

  //shutdown the actor system
  system.terminate()

  StdIn.readLine()
}