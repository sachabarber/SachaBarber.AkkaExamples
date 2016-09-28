package sample.cluster.transformation.frontend

import sample.cluster.transformation.{TransformationResult, BackendRegistration, JobFailed, TransformationJob}
import language.postfixOps
import scala.concurrent.Future
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Terminated
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


import akka.pattern.pipe
import akka.pattern.ask

import scala.util.{Success, Failure}

class TransformationFrontend extends Actor {

  var backends = IndexedSeq.empty[ActorRef]
  var jobCounter = 0

  def receive = {
    case job: TransformationJob if backends.isEmpty =>
      sender() ! JobFailed("Service unavailable, try again later", job)

    case job: TransformationJob =>
      println(s"Frontend saw TransformationJob : '$job'")
      jobCounter += 1
      //backends(jobCounter % backends.size) forward job

      implicit val timeout = Timeout(5 seconds)
      val result : Future[TransformationResult] = (backends(jobCounter % backends.size) ? job).map(x => x.asInstanceOf[TransformationResult])
      result.onComplete {
        case Success(transformationResult) => {
          println(s"Front end saw TransformationResult: $transformationResult trying to send to ${sender.path.address.toString}")
          sender ! transformationResult
        }
        case Failure(t) => println("An error has occured: " + t.getMessage)
      }


//      pipe(result) to sender

    case BackendRegistration if !backends.contains(sender()) =>
      context watch sender()
      backends = backends :+ sender()

    case Terminated(a) =>
      backends = backends.filterNot(_ == a)
  }
}
