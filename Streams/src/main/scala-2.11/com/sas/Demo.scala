package com.sas

import akka.actor._
import akka.stream._
import com.sas.basicflows.BasicFlowDemo
import com.sas.customshapes.CustomShapeDemo
import com.sas.graphs.WritePrimesDemo

import scala.io.StdIn
import scala.language.postfixOps

object Demo extends App {



// val basicFlowDemo = new BasicFlowDemo()
//  basicFlowDemo.simpleFlow()
//  basicFlowDemo.differentSourcesAndSinks()
//  basicFlowDemo.mapFlow()
//  basicFlowDemo.end()
//  val writePrimesDemo = new WritePrimesDemo()
//  writePrimesDemo.run()


  val customShapeDemo = new CustomShapeDemo()
  customShapeDemo.runAccumulateWhileUnchanged()
  customShapeDemo.runDistinctUntilChanged()


  StdIn.readLine()


}