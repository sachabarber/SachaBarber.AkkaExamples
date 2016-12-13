package com.sas.basicflows

import akka.Done
import akka.actor._
import akka.stream._
import akka.stream.scaladsl._
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

class BasicFlowDemo()  {

  implicit val system = ActorSystem("StreamsSystem")
  implicit val materializer = ActorMaterializer()



  def end() : Unit = {
    system.terminate()
  }

  def simpleFlow() : Unit = {
    val source = Source(1 to 10)
    val sink = Sink.fold[Int, Int](0)(_ + _)
    // connect the Source to the Sink, obtaining a RunnableGraph
    val runnable: RunnableGraph[Future[Int]] = source.toMat(sink)(Keep.right)
    // materialize the flow and get the value of the FoldSink
    implicit val timeout = Timeout(5 seconds)
    val sumFuture: Future[Int] = runnable.run()
    val sum = Await.result(sumFuture, timeout.duration)
    println(s"source.toMat(sink)(Keep.right) Sum = $sum")

    // Use the shorthand source.runWith(sink)
    val sumFuture2: Future[Int] = source.runWith(sink)
    val sum2 = Await.result(sumFuture2, timeout.duration)
    println(s"source.runWith(sink) Sum = $sum")
  }


  def differentSourcesAndSinks() : Unit = {
    //various sources
    Source(List(1, 2, 3)).runWith(Sink.foreach(println))
    Source.single("only one element").runWith(Sink.foreach(println))
    //actor sink
    val helloActor = system.actorOf(Props[HelloActor], name = "helloactor")
    Source(List("hello", "hello"))
      .runWith(Sink.actorRef(helloActor,DoneMessage))
    //future source
    val futureString = Source.fromFuture(Future.successful("Hello Streams!"))
      .toMat(Sink.head)(Keep.right).run()
    implicit val timeout = Timeout(5 seconds)
    val theString = Await.result(futureString, timeout.duration)
    println(s"theString = $theString")
  }



  def mapFlow() : Unit = {
    val source = Source(11 to 16)
    val doublerSource = source.map(x => x * 2)
    val sink = Sink.foreach(println)
    implicit val timeout = Timeout(5 seconds)

    // Use the shorthand source.runWith(sink)
    val printSinkFuture: Future[Done] = doublerSource.runWith(sink)
    Await.result(printSinkFuture, timeout.duration)
  }
}