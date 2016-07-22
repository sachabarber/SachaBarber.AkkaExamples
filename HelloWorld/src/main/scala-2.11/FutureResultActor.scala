import akka.actor._
import akka.pattern.pipe
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

class FutureResultActor extends Actor {
  def receive = {
    case GetIdsFromDatabase => {
      Future(List(1,2,3)).pipeTo(sender)
    }
    case _       => println("unknown message")
  }
}