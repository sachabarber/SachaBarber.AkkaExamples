package com.sas.basicflows

import akka.actor.Actor

class HelloActor extends Actor {
  def receive = {
    case "hello" => {
      println("hello message seen by HelloActor")
    }
    case DoneMessage => {
      println(s"DoneMessage seen by HelloActor")
    }
  }
}