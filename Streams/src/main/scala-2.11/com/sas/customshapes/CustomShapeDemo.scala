package com.sas.customshapes

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}


class CustomShapeDemo {
  implicit val system = ActorSystem("StreamsSystem")
  implicit val materializer = ActorMaterializer()

  def run() : Unit = {
    Source(SampleElements.All)
      .via(new AccumulateWhileUnchanged(_.value))
      .runWith(Sink.foreach(println))

    Source(SampleElements.All)
          .via(new DistinctUntilChanged(_.value))
          .runWith(Sink.foreach(println))
  }
}
