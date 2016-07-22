import akka.actor.Actor

class HelloActor extends Actor {
  def receive = {
    case "hello" => println("world")
    case _       => println("unknown message")
  }
}