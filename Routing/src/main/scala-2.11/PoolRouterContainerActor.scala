import akka.actor._
import akka.routing._
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.pattern.ask

class PoolRouterContainerActor(val props: Props, val name :String)  extends Actor  {

  val router: ActorRef = context.actorOf(props, name)

  //  val router: ActorRef =
  //    context.actorOf(ScatterGatherFirstCompletedPool(
  //        5, supervisorStrategy = supervisionStrategy,within = 10.seconds).
  //        props(Props[FibonacciActor]), "router")

  def receive = {
    case WorkMessage =>
      implicit val timeout = Timeout(5 seconds)
      val futureResult = router ? FibonacciNumber(10)
      val (actName,result) = Await.result(futureResult, timeout.duration)

      println(s"FibonacciActor : ($actName) came back with result -> $result")
  }
}
