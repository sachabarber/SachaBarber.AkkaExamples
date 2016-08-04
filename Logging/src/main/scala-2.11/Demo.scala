import akka.actor._
import scala.language.postfixOps
import scala.io.StdIn
import akka.event.Logging

object Demo extends App {

  val runner = new Runner()
  runner.Run()
}