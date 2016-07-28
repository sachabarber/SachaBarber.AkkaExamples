import akka.actor._
import scala.language.postfixOps
import scala.io.StdIn

object Demo extends App {

  //create the actor system
  val system = ActorSystem("SupervisionSystem")

  val isOneForOneStrategy = false

  if(isOneForOneStrategy) {
    val oneForOneSupervisorActor = system.actorOf(Props[OneForOneSupervisorActor], name = "OneForOneSupervisorActor")
    println("sending oneForOneSupervisorActor a 'StartChildren' message")

    oneForOneSupervisorActor ! "StartChildren"
    Thread.sleep(1000)

    oneForOneSupervisorActor ! "MakeRandomChildRestart"
    Thread.sleep(1000)

    oneForOneSupervisorActor ! "TellChildrenToSoundOff"
    Thread.sleep(1000)

    oneForOneSupervisorActor ! "MakeRandomChildCommitSuicide"
    Thread.sleep(1000)

    oneForOneSupervisorActor ! "TellChildrenToSoundOff"
    Thread.sleep(1000)

    system.stop(oneForOneSupervisorActor)
  }
  else {
    val allForOneSupervisorActor = system.actorOf(Props[AllForOneSupervisorActor], name = "AllForOneSupervisorActor")
    println("sending allForOneSupervisorActor a 'StartChildren' message")

    allForOneSupervisorActor ! "StartChildren"
    Thread.sleep(1000)

    allForOneSupervisorActor ! "MakeRandomChildRestart"
    Thread.sleep(1000)

    allForOneSupervisorActor ! "TellChildrenToSoundOff"
    Thread.sleep(1000)

    allForOneSupervisorActor ! "MakeRandomChildCommitSuicide"
    Thread.sleep(1000)

    allForOneSupervisorActor ! "TellChildrenToSoundOff"
    Thread.sleep(1000)

    system.stop(allForOneSupervisorActor)
  }


  StdIn.readLine()

  //shutdown the actor system
  system.terminate()

  StdIn.readLine()
}