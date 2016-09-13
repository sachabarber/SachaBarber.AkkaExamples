import akka.actor._
import scala.language.postfixOps
import scala.io.StdIn

object Demo extends App {

  RunDemo

  def RunDemo : Unit = {
    //create the actor system
    val system = ActorSystem("StateMachineSystem")

    val exampleActor =
      system.actorOf(Props(classOf[HelloActor]),
        "demo-exampleActor")

    StdIn.readLine()

    //shutdown the actor system
    system.terminate()

    StdIn.readLine()
  }





}