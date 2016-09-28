import akka.actor.{Props, ActorSystem}
import scala.io.StdIn


object DemoClient {
  def main(args : Array[String]) {

    val system = ActorSystem("OTHERSYSTEM")
    val clientJobTransformationSendingActor = system.actorOf(Props[ClientJobTransformationSendingActor], name = "clientJobTransformationSendingActor")
    clientJobTransformationSendingActor ! Start
    StdIn.readLine()
    system.terminate()
  }
}




