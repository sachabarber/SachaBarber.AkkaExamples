import akka.actor.Actor
import scala.annotation.tailrec

class FibonacciActor extends Actor {

  val actName = self.path.name

  def receive = {
    case FibonacciNumber(nbr) => {
      println(s"FibonacciActor : ($actName) ->  " +
        s"has been asked to calculate FibonacciNumber")
      val result = fibonacci(nbr)
      sender ! (actName,result)
    }
  }

  private def fibonacci(n: Int): Int = {
    @tailrec
    def fib(n: Int, b: Int, a: Int): Int = n match {
      case 0 => a
      case _ => fib(n - 1, a + b, b)
    }

    fib(n, 1, 0)
  }
}