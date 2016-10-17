import java.util.concurrent.atomic.AtomicInteger
import akka.actor.{Actor, Props, Terminated}
import akka.routing.{ ActorRefRoutee, RoundRobinRoutingLogic, Router }


class RoundRobinRouterActor  extends Actor  {

  val counter : AtomicInteger = new AtomicInteger()

  val routees = Vector.fill(5) {
    val workerCount = counter.getAndIncrement()
    val r = context.actorOf(Props(
      new WorkerActor(workerCount)), name = s"workerActor-$workerCount")
    context watch r
    ActorRefRoutee(r)
  }

  var router = Router(RoundRobinRoutingLogic(), routees)

  def receive = {
    case WorkMessage =>
      router.route(WorkMessage, sender())
    case Report => routees.foreach(ref => ref.send(Report, sender()))
    case Terminated(a) =>
      router = router.removeRoutee(a)
      val workerCount = counter.getAndIncrement()
      val r = context.actorOf(Props(
        new WorkerActor(workerCount)), name = s"workerActor-$workerCount")
      context watch r
      router = router.addRoutee(r)
  }
}
