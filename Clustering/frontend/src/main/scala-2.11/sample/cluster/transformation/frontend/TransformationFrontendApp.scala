package sample.cluster.transformation.frontend

import sample.cluster.transformation.{TransformationJob}
import language.postfixOps
import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import java.util.concurrent.atomic.AtomicInteger



object TransformationFrontendApp {

  def main(args: Array[String]): Unit = {

    // Override the configuration of the port when specified as program argument
    val port = if (args.isEmpty) "0" else args(0)
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.parseString("akka.cluster.roles = [frontend]")).
      withFallback(ConfigFactory.load())

    val system = ActorSystem("ClusterSystem", config)
    val frontend = system.actorOf(Props[TransformationFrontend], name = "frontend")

    val counter = new AtomicInteger
    import system.dispatcher
    system.scheduler.schedule(2.seconds, 2.seconds) {
      implicit val timeout = Timeout(5 seconds)
      (frontend ? TransformationJob("hello-" + counter.incrementAndGet())) onSuccess {
        case result => println(result)
      }
    }
  }

}