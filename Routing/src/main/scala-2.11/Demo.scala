import java.util.concurrent.TimeUnit

import akka.actor._
import akka.routing._
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.io.StdIn

object Demo extends App {

  //==============================================================
  //Standard Actor that does routing using Router class
  //where we apply relevant RoutingLogic
  //Supervision is done manually within the Actor that hosts
  //the Router, where we monitor the routees and remove /recreate
  //them on 'Terminated'
  //
  //    Comment/Uncomment to try the different router logic
  //
  //==============================================================
  //RunRoutingDemo(RoundRobinRoutingLogic())
  //RunRoutingDemo(RandomRoutingLogic())
  //RunRoutingDemo(SmallestMailboxRoutingLogic())
  //RunRoutingDemo(BroadcastRoutingLogic())



  //==============================================================
  // Use built Pool router(s) which will do the supervision for us
  //
  //
  //    Comment/Uncomment to try the different router logic
  //
  //==============================================================
  RunScatterGatherFirstCompletedPoolDemo()
  //RunTailChoppingPoolDemo()






  def RunRoutingDemo(routingLogic : RoutingLogic) : Unit = {
    val system = ActorSystem("RoutingSystem")
    val actorRef = system.actorOf(Props(
      new RouterActor(routingLogic)), name = "theRouter")

    for (i <- 0 until 10) {
      actorRef ! WorkMessage
      Thread.sleep(1000)
    }
    actorRef ! Report

    StdIn.readLine()
    system.terminate()
  }


  def RunScatterGatherFirstCompletedPoolDemo() : Unit = {

    val supervisionStrategy = OneForOneStrategy() {
      case e => SupervisorStrategy.restart
    }

    val props = ScatterGatherFirstCompletedPool(
      5, supervisorStrategy = supervisionStrategy,within = 10.seconds).
      props(Props[FibonacciActor])

    RunPoolDemo(props)
  }

  def RunTailChoppingPoolDemo() : Unit = {

    val supervisionStrategy = OneForOneStrategy() {
      case e => SupervisorStrategy.restart
    }

    val props = TailChoppingPool(5, within = 10.seconds,
      supervisorStrategy = supervisionStrategy,interval = 20.millis).
      props(Props[FibonacciActor])

    RunPoolDemo(props)
  }





  def RunPoolDemo(props : Props) : Unit = {
    val system = ActorSystem("RoutingSystem")
    val actorRef = system.actorOf(Props(new PoolRouterContainerActor(props,"theRouter")), name = "thePoolContainer")
    actorRef ! WorkMessage
    StdIn.readLine()
    system.terminate()
  }
}