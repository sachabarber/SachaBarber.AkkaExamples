import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.io.StdIn
import scala.util.{Success, Failure}
import ExecutionContext.Implicits.global

object Demo extends App {

  //create the actor system
  val system = ActorSystem("HelloSystem")

  // default Actor constructor
  val helloActor = system.actorOf(Props[HelloActor], name = "helloactor")
  val askActor = system.actorOf(Props[AskActor], name = "askactor")
  val futureResultActor = system.actorOf(Props[FutureResultActor], name = "futureresultactor")

  //send some messages to the HelloActor
  helloActor ! "hello"
  helloActor ! "tis a fine day for Akka"

  //send some messages to the AskActor, we want a response from it

  // (1) this is one way to "ask" another actor for information
  implicit val timeout = Timeout(5 seconds)
  val future1 = askActor ? GetDateMessage
  val result1 = Await.result(future1, timeout.duration).asInstanceOf[String]
  println(s"result1=$result1")

  // (2) a slightly different way to ask another actor for information
  val future2: Future[String] = ask(askActor, GetDateMessage).mapTo[String]
  val result2 = Await.result(future2, 5 second)
  println(s"result2=$result2")

  // (3) don't use blocking call at all, just use future callbacks
  val future3: Future[String] = ask(askActor, GetDateMessage).mapTo[String]
  future3 onComplete {
    case Success(result3) =>  println(s"result3=$result3")
    case Failure(t) => println("An error has occured: " + t.getMessage)
  }


  //send some messages to the FutureResultActor, we expect a Future back from it
  val future4: Future[List[Int]] = ask(futureResultActor, GetIdsFromDatabase).mapTo[List[Int]]
  future4 onComplete {
    case Success(result4) =>  println(s"result4=$result4")
    case Failure(t) => println("An error has occured: " + t.getMessage)
  }


  //shutdown the actor system
  system.terminate()

  StdIn.readLine()
}