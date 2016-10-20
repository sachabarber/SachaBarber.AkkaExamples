import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{Actor, Props, Terminated}
import akka.routing.{RoutingLogic, ActorRefRoutee, RoundRobinRoutingLogic, Router}


class RouterActor(val routingLogic : RoutingLogic)  extends Actor  {

  val counter : AtomicInteger = new AtomicInteger()

  val routees = Vector.fill(5) {
    val workerCount = counter.getAndIncrement()
    val r = context.actorOf(Props(
      new WorkerActor(workerCount)), name = s"workerActor-$workerCount")
    context watch r
    ActorRefRoutee(r)
  }

  //create a Router based on the incoming class field
  //RoutingLogic which will really determine what type of router
  //we end up with
  var router = Router(routingLogic, routees)

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
