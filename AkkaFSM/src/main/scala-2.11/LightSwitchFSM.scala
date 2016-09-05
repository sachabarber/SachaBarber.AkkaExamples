import akka.actor.{Actor, ActorRef, FSM}
import scala.concurrent.duration._
import scala.collection._

// received events
final case class PowerOn()
final case class PowerOff()

// states
sealed trait LightSwitchState
case object On extends LightSwitchState
case object Off extends LightSwitchState

//data
sealed trait LightSwitchData
case object NoData extends LightSwitchData

class LightSwitchActor extends FSM[LightSwitchState, LightSwitchData] {

  startWith(Off, NoData)

  when(Off) {
    case Event(PowerOn, _) =>
      goto(On) using NoData
  }

  when(On, stateTimeout = 1 second) {
    case Event(PowerOff, _) =>
      goto(Off) using NoData
    case Event(StateTimeout, _) =>
      println("'On' state timed out, moving to 'Off'")
      goto(Off) using NoData
  }

  whenUnhandled {
    case Event(e, s) =>
      log.warning("received unhandled request {} in state {}/{}", e, stateName, s)
      goto(Off) using NoData
  }

  onTransition {
    case Off -> On => println("Moved from Off to On")
    case On -> Off => println("Moved from On to Off")
  }

  initialize()
}
