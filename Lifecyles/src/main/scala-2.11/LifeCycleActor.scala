import akka.actor.Actor

class LifeCycleActor extends Actor {
  println("LifeCycleActor: constructor")

  override def preStart { println("LifeCycleActor: preStart") }

  override def postStop { println("LifeCycleActor: postStop") }

  override def preRestart(reason: Throwable, message: Option[Any]) {
    println("LifeCycleActor: preRestart")
    println(s"LifeCycleActor reason: ${reason.getMessage}")
    println(s"LifeCycleActor message: ${message.getOrElse("")}")
    super.preRestart(reason, message)
  }
  override def postRestart(reason: Throwable) {
    println("LifeCycleActor: postRestart")
    println(s"LifeCycleActor reason: ${reason.getMessage}")
    super.postRestart(reason)
  }
  def receive = {
    case RestartMessage => throw new Exception("RestartMessage seen")
    case _ => println("LifeCycleActor : got a message")
  }
}