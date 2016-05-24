package com.kczulko

import akka.actor.{ActorSystem, Props}

object Main {
  def main(args: Array[String]) {
    implicit val system = ActorSystem("FeedsConsumer")
    system.actorOf(Props(new IntcFeedsConsumer))
  }
}
