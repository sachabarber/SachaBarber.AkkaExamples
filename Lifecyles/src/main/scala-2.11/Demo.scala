import akka.actor._
import scala.language.postfixOps
import scala.io.StdIn

object Demo extends App {

  //create the actor system
  val system = ActorSystem("LifeCycleSystem")

  // default Actor constructor
  val lifeCycleActor = system.actorOf(Props[LifeCycleActor], name = "lifecycleactor")


  println("sending lifeCycleActor a number")
  lifeCycleActor ! 100
  Thread.sleep(1000)

  println("force restart")
  lifeCycleActor ! RestartMessage
  Thread.sleep(1000)

  println("stop lifeCycleActor")
  system.stop(lifeCycleActor)

  //shutdown the actor system
  system.terminate()

  StdIn.readLine()
}