package com.sas.customshapes

import akka.stream.stage.{GraphStageLogic, InHandler, OutHandler, GraphStage}
import akka.stream.{Outlet, Attributes, Inlet, FlowShape}

import scala.collection.immutable

//https://www.softwaremill.com/implementing-a-custom-akka-streams-graph-stage/
final class AccumulateWhileUnchanged[E, P](propertyExtractor: E => P)
  extends GraphStage[FlowShape[E, immutable.Seq[E]]] {

  val in = Inlet[E]("AccumulateWhileUnchanged.in")
  val out = Outlet[immutable.Seq[E]]("AccumulateWhileUnchanged.out")

  override def shape = FlowShape.of(in, out)

  override def createLogic(attributes: Attributes) = new GraphStageLogic(shape) {

    private var currentState: Option[P] = None
    private val buffer = Vector.newBuilder[E]

    setHandlers(in, out, new InHandler with OutHandler {

      override def onPush(): Unit = {
        val nextElement = grab(in)
        val nextState = propertyExtractor(nextElement)

        if (currentState.isEmpty || currentState.contains(nextState)) {
          buffer += nextElement
          pull(in)
        } else {
          val result = buffer.result()
          buffer.clear()
          buffer += nextElement
          push(out, result)
        }

        currentState = Some(nextState)
      }

      override def onPull(): Unit = {
        pull(in)
      }

      override def onUpstreamFinish(): Unit = {
        val result = buffer.result()
        if (result.nonEmpty) {
          emit(out, result)
        }
        completeStage()
      }
    })

    override def postStop(): Unit = {
      buffer.clear()
    }
  }
}



