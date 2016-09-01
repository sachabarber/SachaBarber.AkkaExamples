import akka.actor._
import scala.language.postfixOps
import scala.io.StdIn

object Demo extends App {

  //uncomment / comment out as required to run
  //the demos
  //RunHotColdStateDemo
  RunBecomeUnbecomeStateDemo

  def RunHotColdStateDemo : Unit = {
    //create the actor system
    val system = ActorSystem("StateMachineSystem")

    val hotColdStateActor =
      system.actorOf(Props(classOf[HotColdStateActor]),
        "demo-HotColdStateActor")


    println("sending sun")
    hotColdStateActor ! "sun"
    //actors are async, so give it chance to get message
    //obviously we would not do this in prod code, its just
    //for the demo, to get the correct ordering for the print
    //statements
    Thread.sleep(1000)

    println("sending sun")
    hotColdStateActor ! "sun"
    Thread.sleep(1000)

    println("sending snow")
    hotColdStateActor ! "snow"
    Thread.sleep(1000)

    println("sending snow")
    hotColdStateActor ! "snow"
    Thread.sleep(1000)

    println("sending sun")
    hotColdStateActor ! "sun"
    Thread.sleep(1000)

    println("sending snow")
    hotColdStateActor ! "snow"
    Thread.sleep(1000)




    StdIn.readLine()

    //shutdown the actor system
    system.terminate()

    StdIn.readLine()
  }

  def RunBecomeUnbecomeStateDemo : Unit = {
    //create the actor system
    val system = ActorSystem("StateMachineSystem")

    val becomeUnbecomeStateActor =
      system.actorOf(Props(classOf[BecomeUnbecomeStateActor]),
        "demo-BecomeUnbecomeStateActor")

    println("Sending snow")
    becomeUnbecomeStateActor ! "snow"
    //actors are async, so give it chance to get message
    Thread.sleep(1000)

    println("Sending snow")
    becomeUnbecomeStateActor ! "snow"
    Thread.sleep(1000)

    println("Sending sun")
    becomeUnbecomeStateActor ! "sun"
    Thread.sleep(1000)

    println("Sending sun")
    becomeUnbecomeStateActor ! "sun"
    Thread.sleep(1000)

    println("Sending snow")
    becomeUnbecomeStateActor ! "snow"
    Thread.sleep(1000)

    StdIn.readLine()

    //shutdown the actor system
    system.terminate()

    StdIn.readLine()
  }

}