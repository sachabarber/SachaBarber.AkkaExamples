import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import scala.concurrent.{Await, Future}
import concurrent.ExecutionContext.Implicits.global
import common.{Item, JsonSupport}
import concurrent.duration._
import scala.io.StdIn

object Demo extends App with JsonSupport {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val httpClient = Http().outgoingConnection(host = "localhost", port = 8080)

  //+++++++++++++++++++++++++++++++++++++++++++++++
  // GET http://localhost:8080/randomitem
  //+++++++++++++++++++++++++++++++++++++++++++++++
  val randomItemUrl = s"""/randomitem"""
  private val flowGet : Future[Item] =
    Source.single(HttpRequest(method = HttpMethods.GET,uri = Uri(randomItemUrl)))
    .via(httpClient)
    .mapAsync(1)(response => Unmarshal(response.entity).to[Item])
    .runWith(Sink.head)
  val start = System.currentTimeMillis()
  val result = Await.result(flowGet, 5 seconds)
  val end = System.currentTimeMillis()
  println(s"Result in ${end-start} millis: $result")


  //+++++++++++++++++++++++++++++++++++++++++++++++
  // POST http://localhost:8080/saveitem
  //+++++++++++++++++++++++++++++++++++++++++++++++
  val saveItemUrl = s"""/saveitem"""
  val itemToSave = Item("newItemHere",12)
  val flowPost = for {
    requestEntity <- Marshal(itemToSave).to[RequestEntity]
    response <-
            Source.single(HttpRequest(method = HttpMethods.POST,uri = Uri(saveItemUrl), entity = requestEntity))
            .via(httpClient)
            .mapAsync(1)(response => Unmarshal(response.entity).to[Item])
            .runWith(Sink.head)
  } yield response
  val startPost = System.currentTimeMillis()
  val resultPost = Await.result(flowPost, 5 seconds)
  val endPost = System.currentTimeMillis()
  println(s"Result in ${endPost-startPost} millis: $resultPost")


  StdIn.readLine()
}