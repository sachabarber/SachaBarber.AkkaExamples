import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import akka.util.Timeout
import org.scalatest._
import akka.testkit.TestFSMRef
import akka.actor.FSM

import scala.concurrent.duration._
import scala.util.Success


class LightSwitchFSMActorTests
  extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll
  with Matchers {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "An LightSwitchActor " must {
    "start in the 'Off' state" in {
      val fsm = TestFSMRef(new LightSwitchActor())
      assert(fsm.stateName == Off)
      assert(fsm.stateData == NoData)
    }
  }

  "An LightSwitchActor that starts with 'Off' " must {
    "should transition to 'On' when told to by the test" in {
      val fsm = TestFSMRef(new LightSwitchActor())
      fsm.setState(stateName = On)
      assert(fsm.stateName == On)
      assert(fsm.stateData == NoData)
    }
  }

  "An LightSwitchActor that starts with 'Off' " must {
    "should transition to 'On' when sent a 'PowerOn' message" in {
      val fsm = TestFSMRef(new LightSwitchActor())
      fsm ! PowerOn
      assert(fsm.stateName == On)
      assert(fsm.stateData == NoData)
    }
  }

  "An LightSwitchActor that stays 'On' for more than 1 second " must {
    "should transition to 'Off' thanks to the StateTimeout" in {
      val fsm = TestFSMRef(new LightSwitchActor())
      fsm ! PowerOn
      awaitCond(fsm.stateName == Off, 1200 milliseconds, 100 milliseconds)
    }
  }
}


