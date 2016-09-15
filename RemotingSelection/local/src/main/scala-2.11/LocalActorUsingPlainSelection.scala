import java.util.concurrent.atomic.AtomicInteger
import akka.actor.{Terminated, ActorRef, ReceiveTimeout, Actor}
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

case object Init

class LocalActorUsingPlainSelection extends Actor {

  val path = "akka.tcp://RemoteDemoSystem@127.0.0.1:4444/user/RemoteActor"
  val atomicInteger = new AtomicInteger();
  context.setReceiveTimeout(3 seconds)

  def receive = identifying

  def identifying: Receive = {
    case Init => {
      implicit val resolveTimeout = Timeout(5 seconds)
      for (ref : ActorRef <- context.actorSelection(path).resolveOne()) {
        println("Resolved remote actor ref using Selection")
        context.watch(ref)
        context.become(active(ref))
        context.setReceiveTimeout(Duration.Undefined)
        self ! Start
      }
    }
    case ReceiveTimeout => println("timeout")
  }

  def active(actor: ActorRef): Receive = {
    case Start =>
      actor ! "Hello from the LocalActorUsingPlainSelection"
    case msg: String =>
      println(s"LocalActorUsingPlainSelection received message: '$msg'")
      if (atomicInteger.get() < 5) {
        sender ! "Hello back to you"
        atomicInteger.getAndAdd(1)
      }
    case Terminated(`actor`) =>
      println("Receiver terminated")
      context.system.terminate()
  }
}
