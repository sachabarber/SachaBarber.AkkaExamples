import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{Props, ActorSystem}
import akka.util.Timeout
import scala.io.StdIn
import scala.concurrent.duration._


object DemoClient {
  def main(args : Array[String]) {

    val system = ActorSystem("OTHERSYSTEM")
    val clientJobTransformationSendingActor =
      system.actorOf(Props[ClientJobTransformationSendingActor],
        name = "clientJobTransformationSendingActor")

    val counter = new AtomicInteger
    import system.dispatcher
    system.scheduler.schedule(2.seconds, 2.seconds) {
      clientJobTransformationSendingActor ! Send(counter.incrementAndGet())
      Thread.sleep(1000)
    }

    StdIn.readLine()
    system.terminate()
  }
}




