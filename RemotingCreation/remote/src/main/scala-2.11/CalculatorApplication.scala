import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem

object CalculatorApplication {

  def main(args: Array[String]): Unit = {
    startRemoteWorkerSystem()
  }

  def startRemoteWorkerSystem(): Unit = {
    ActorSystem("CalculatorWorkerSystem", ConfigFactory.load("calculator"))
    println("Started CalculatorWorkerSystem")
  }

}
