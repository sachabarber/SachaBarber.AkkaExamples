import akka.actor.Actor


class HelloActor extends Actor {
  def receive = {
    case "hello" => sender ! "hello world"
    case _       => throw new IllegalArgumentException("bad juju")
  }
}

