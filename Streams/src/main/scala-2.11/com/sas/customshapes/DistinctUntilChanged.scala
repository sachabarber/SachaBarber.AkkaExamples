package com.sas.customshapes

import akka.stream.stage.{GraphStageLogic, InHandler, OutHandler, GraphStage}
import akka.stream.{Outlet, Attributes, Inlet, FlowShape}

import scala.collection.immutable

final class DistinctUntilChanged[E, P](propertyExtractor: E => P)
  extends GraphStage[FlowShape[E, E]] {

  val in = Inlet[E]("DistinctUntilChanged.in")
  val out = Outlet[E]("DistinctUntilChanged.out")

  override def shape = FlowShape.of(in, out)

  override def createLogic(attributes: Attributes) = new GraphStageLogic(shape) {

    private var savedState : Option[E] = None

    setHandlers(in, out, new InHandler with OutHandler {

      override def onPush(): Unit = {
        val nextElement = grab(in)
        val nextState = propertyExtractor(nextElement)

        if (savedState.isEmpty  || propertyExtractor(savedState.get) != nextState) {
          savedState = Some(nextElement)
          push(out, savedState.get)
        }
        else {
          pull(in)
        }
        savedState = Some(nextElement)
      }

      override def onPull(): Unit = {
        pull(in)
      }

      override def onUpstreamFinish(): Unit = {
        completeStage()
      }
    })

    override def postStop(): Unit = {
      savedState = None
    }
  }
}



