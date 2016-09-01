import akka.actor.Actor

class HotColdStateActor extends Actor {

  //need this for become/unbecome
  import context._

  def cold: Receive = {
    case "snow" => println("I am already cold!")
    case "sun" => becomeHot
  }

  def hot: Receive = {
    case "sun" => println("I am already hot!")
    case "snow" => becomeCold
  }

  def receive = {
    case "snow" => becomeCold
    case "sun" => becomeHot
  }


  private def becomeCold: Unit = {
    println("becoming cold")
    become(cold)
  }

  private def becomeHot: Unit = {
    println("becoming hot")
    become(hot)
  }
}
