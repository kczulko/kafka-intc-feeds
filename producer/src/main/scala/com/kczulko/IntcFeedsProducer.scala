package com.kczulko

import java.lang.System.getProperty

import akka.actor.ActorLogging
import akka.stream.ActorMaterializer
import akka.stream.actor.{ActorPublisher, ActorPublisherMessage}
import org.joda.time.DateTime

import scala.annotation.tailrec
import scala.util.Random

class IntcFeedsProducer extends ActorPublisher[MarketStockMessage] with ActorLogging {

  implicit lazy val materializer = ActorMaterializer()(context)

  val varianceValue = 0.2

  lazy val companyId = Option(getProperty("companyId")).getOrElse("INTC")
  lazy val stockValue = Option(getProperty("initialValue"))
                                .map(_.toDouble)
                                .getOrElse(31.5)

  private def publishMessages() = {
    @tailrec
    def publish(msg: MarketStockMessage): MarketStockMessage = {
      Thread.sleep(2000)

      val message: MarketStockMessage = MarketStockMessage(
        msg.companyId,
        DateTime.now().toString,
        msg.stockValue + varianceValue*(Random.nextInt(3) - 1)
      )
      if (!isActive || totalDemand <= 0) {
        return message
      }

      log.info(s"Publishing: $msg")
      onNext(msg)
      publish(message)
    }

    publish(MarketStockMessage(companyId, DateTime.now().toString, stockValue))
  }

  override def receive: Receive = {
    case ActorPublisherMessage.Request(_) => publishMessages()
    case ActorPublisherMessage.Cancel => context.stop(self)
    case _ =>
  }
}
