import java.util.concurrent.atomic.AtomicInteger
import akka.actor._
import scala.concurrent.duration._


case object Start


class LocalActorUsingIdentity extends Actor {

  val path = "akka.tcp://RemoteDemoSystem@127.0.0.1:4444/user/RemoteActor"
  val atomicInteger = new AtomicInteger();
  context.setReceiveTimeout(3 seconds)
  sendIdentifyRequest()

  def receive = identifying

  def sendIdentifyRequest(): Unit =
    context.actorSelection(path) ! Identify(path)

  def identifying: Receive = {
    case identity : ActorIdentity =>
      if(identity.correlationId.equals(path)) {
        identity.ref match {
          case Some(remoteRef) => {
            context.watch(remoteRef)
            context.become(active(remoteRef))
            context.setReceiveTimeout(Duration.Undefined)
            self ! Start
          }
          case None => println(s"Remote actor not available: $path")
        }
      }
    case ReceiveTimeout => sendIdentifyRequest()
  }

  def active(actor: ActorRef): Receive = {
    case Start =>
      actor ! "Hello from the LocalActorUsingIdentity"
    case msg: String =>
      println(s"LocalActorUsingIdentity received message: '$msg'")
      if (atomicInteger.get() < 5) {
        sender ! "Hello back to you"
        atomicInteger.getAndAdd(1)
      }
    case Terminated(`actor`) =>
      println("Receiver terminated")
      context.system.terminate()
  }
}