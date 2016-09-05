import akka.actor.{Actor, ActorRef, FSM}
import scala.concurrent.duration._
import scala.collection._

// received events
final case class SetTarget(ref: ActorRef)
final case class Queue(obj: Any)
case object Flush

// sent events
final case class Batch(obj: immutable.Seq[Any])

// states
sealed trait State
case object Idle extends State
case object Active extends State

//data
sealed trait BuncherData
case object Uninitialized extends BuncherData
final case class Todo(target: ActorRef, queue: immutable.Seq[Any]) extends BuncherData

class BuncherActor extends FSM[State, BuncherData] {

  startWith(Idle, Uninitialized)

  when(Idle) {
    case Event(SetTarget(ref), Uninitialized) =>
      stay using Todo(ref, Vector.empty)
  }

  when(Active, stateTimeout = 1 second) {
    case Event(Flush | StateTimeout, t: Todo) =>
      goto(Idle) using t.copy(queue = Vector.empty)
  }

  whenUnhandled {
    // common code for both states
    case Event(Queue(obj), t @ Todo(_, v)) =>
      goto(Active) using t.copy(queue = v :+ obj)

    case Event(e, s) =>
      log.warning("received unhandled request {} in state {}/{}", e, stateName, s)
      stay
  }

  onTransition {
    case Active -> Idle =>
      stateData match {
        case Todo(ref, queue) => ref ! Batch(queue)
        case _                => // nothing to do
      }
  }

  initialize()
}


class BunchReceivingActor extends Actor {
  def receive = {
    case Batch(theBatchData) => {
      println(s"receiving the batch data $theBatchData")
    }
    case _ => println("unknown message")
  }
}