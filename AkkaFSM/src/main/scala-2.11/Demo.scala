import akka.actor._
import scala.language.postfixOps
import scala.io.StdIn

object Demo extends App {

  //uncomment / comment out as required to run
  //the demos
  //RunBuncherDemo
  RunLightSwitchDemo

  def RunBuncherDemo : Unit = {
    //create the actor system
    val system = ActorSystem("StateMachineSystem")

    val buncherActor =
      system.actorOf(Props(classOf[BuncherActor]),
        "demo-Buncher")

    val bunchReceivingActor =
      system.actorOf(Props(classOf[BunchReceivingActor]),
        "demo-BunchReceiving")

    buncherActor ! SetTarget(bunchReceivingActor)

    println("sending Queue(42)")
    buncherActor ! Queue(42)
    println("sending Queue(43)")
    buncherActor ! Queue(43)
    println("sending Queue(44)")
    buncherActor ! Queue(44)
    println("sending Flush")
    buncherActor ! Flush
    println("sending Queue(45)")
    buncherActor ! Queue(45)


    StdIn.readLine()

    //shutdown the actor system
    system.terminate()

    StdIn.readLine()
  }

  def RunLightSwitchDemo : Unit = {
    //create the actor system
    val system = ActorSystem("StateMachineSystem")

    val lightSwitchActor =
      system.actorOf(Props(classOf[LightSwitchActor]),
        "demo-LightSwitch")


    println("sending PowerOff, should be off already")
    lightSwitchActor ! PowerOff
    //akka is async allow it some time to pick up message
    //from its mailbox
    Thread.sleep(500)


    println("sending PowerOn")
    lightSwitchActor ! PowerOn
    //akka is async allow it some time to pick up message
    //from its mailbox
    Thread.sleep(500)

    println("sending PowerOff")
    lightSwitchActor ! PowerOff
    //akka is async allow it some time to pick up message
    //from its mailbox
    Thread.sleep(500)


    println("sending PowerOn")
    lightSwitchActor ! PowerOn
    //akka is async allow it some time to pick up message
    //from its mailbox
    Thread.sleep(500)

    println("sleep for a while to allow 'On' state to timeout")
    Thread.sleep(2000)

    StdIn.readLine()

    //shutdown the actor system
    system.terminate()

    StdIn.readLine()
  }



}