import akka.actor._
import scala.language.postfixOps
import scala.io.StdIn

object Demo extends App {

  RunRoundRobinDemo()

  def RunRoundRobinDemo() : Unit = {

    val system = ActorSystem("HelloSystem")
    val actorRef = system.actorOf(Props[RoundRobinRouterActor],
      "roundRobinRouterActor")
    for (i <- 0 until 10) {
      actorRef ! WorkMessage
      Thread.sleep(1000)
    }
    actorRef ! Report

    StdIn.readLine()
    system.terminate()
  }
}