import akka.actor.Actor

class AskActor extends Actor {
  def receive = {
    case GetDateMessage => sender ! new org.joda.time.DateTime().toDate().toString()
    case _       => println("unknown message")
  }
}