import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.{TestProbe, ImplicitSender, TestActorRef, TestKit}
import akka.util.Timeout
import org.scalatest._

import scala.concurrent.duration._
import scala.util.Success


class DoubleSenderActorTestsUsingProbe
  extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll
  with Matchers {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "An DoubleSenderActor that has 2 target ActorRef for sending messages to " must {
    "should send messages to both supplied 'TestProbe(s)'" in {
      val doubleSenderActor = system.actorOf(Props[DoubleSenderActor],
        name = "multiSenderActor")
      val probe1 = TestProbe()
      val probe2 = TestProbe()
      doubleSenderActor ! ((probe1.ref, probe2.ref))
      doubleSenderActor ! "hello"
      probe1.expectMsg(500 millis, "hello")
      probe2.expectMsg(500 millis, "hello")
    }
  }
}


