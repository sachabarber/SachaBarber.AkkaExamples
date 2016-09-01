import akka.actor.Actor

class BecomeUnbecomeStateActor extends Actor {

  //need this for become/unbecome
  import context._

  def receive = {
    case "snow" =>
      println("saw snow, becoming")
      become({
        case "sun" =>
          println("saw sun, unbecoming")
          unbecome() // resets the latest 'become' (just for fun)
        case _ => println("Unknown message, state only likes sun")
      }, discardOld = false) // push on top instead of replace

    case _ => println("Unknown message, state only likes snow")
  }



}
