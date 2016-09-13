import akka.actor.{Props, ActorSystem}
import akka.util.Timeout
import org.scalatest._
import akka.testkit.{ImplicitSender, TestKit, TestActorRef}
import scala.concurrent.duration._
import scala.concurrent.Await
import akka.pattern.ask

import scala.util.Success


class HelloActorTests
  extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll
  with Matchers {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "An HelloActor using implicit sender " must {
    "send back 'hello world'" in {
      val helloActor = system.actorOf(Props[HelloActor], name = "helloActor")
      helloActor ! "hello"
      expectMsg("hello world")
    }
  }

  "An HelloActor using TestActorRef " must {
    "send back 'hello world' when asked" in {
      implicit val timeout = Timeout(5 seconds)
      val helloActorRef = TestActorRef(new HelloActor)
      val future = helloActorRef ? "hello"
      val Success(result: String) = future.value.get
      result should be("hello world")
    }
  }

  "An HelloActor using TestActorRef " must {
    "should throw IllegalArgumentException when sent unhandled message" in {
      val actorRef = TestActorRef(new HelloActor)
      intercept[IllegalArgumentException] { actorRef.receive("should blow up") }
    }
  }


}


