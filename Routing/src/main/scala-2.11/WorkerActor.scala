import akka.actor.Actor

class WorkerActor(val id : Int) extends Actor {

  var msgCount = 0
  val actName = self.path.name

  def receive = {
    case WorkMessage => {
      msgCount += 1
      println(s"worker : {$id}, name : ($actName) ->  ($msgCount)")
    }
    case Report => {
      println(s"worker : {$id}, name : ($actName) ->  saw total messages : ($msgCount)")
    }
    case _       => println("unknown message")
  }
}