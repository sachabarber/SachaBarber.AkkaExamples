import akka.actor._
import scala.language.postfixOps
import scala.io.StdIn
import akka.event.Logging

class Runner {

  def Run(): Unit = {

    //create the actor system
    val system = ActorSystem("LifeCycleSystem")

    // default Actor constructor
    val lifeCycleActor =
      system.actorOf(Props[LifeCycleActor],
        name = "lifecycleactor")
    val lifeCycleActorWithLoggingTrait =
      system.actorOf(Props[LifeCycleActorWithLoggingTrait],
        name = "lifecycleactorwithloggintrait")

    val log = Logging(system, classOf[Runner])
    log.debug("Runner IS UP BABY")

    log.debug("sending lifeCycleActor a number")
    lifeCycleActor ! 100
    lifeCycleActorWithLoggingTrait ! 200
    Thread.sleep(1000)

    log.debug("force restart")
    lifeCycleActor ! RestartMessage
    lifeCycleActorWithLoggingTrait ! RestartMessage
    Thread.sleep(1000)

    log.debug("stop lifeCycleActor")
    system.stop(lifeCycleActor)
    system.stop(lifeCycleActorWithLoggingTrait)

    //shutdown the actor system
    log.debug("stop actor system")
    system.terminate()

    StdIn.readLine()
  }
}
