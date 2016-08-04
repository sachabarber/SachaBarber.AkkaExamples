import akka.actor.{DeadLetter, Actor}

class DeadLetterMonitorActor
  extends Actor
  with akka.actor.ActorLogging {
  log.info("DeadLetterMonitorActor: constructor")

  def receive = {
    case d: DeadLetter => {
      log.error(s"DeadLetterMonitorActor : saw dead letter $d")
    }
    case _ => log.info("DeadLetterMonitorActor : got a message")
  }
}
