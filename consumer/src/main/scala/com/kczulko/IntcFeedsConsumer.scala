package com.kczulko

import java.util.UUID

import akka.actor.{Actor, ActorLogging}
import akka.kafka.ConsumerSettings
import akka.kafka.scaladsl.Consumer
import akka.stream.ActorMaterializer
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.ByteArrayDeserializer

class IntcFeedsConsumer extends Actor with ActorLogging {

  lazy val companyId = Option(System.getProperty("companyId")).getOrElse("INTC")
  lazy val kafkaBrokers = Option(System.getProperty("brokers")).getOrElse("localhost:9092")
  lazy val stockThreshold = Option(System.getProperty("stockThreshold"))
                              .map(_.toDouble)
                              .getOrElse(30.0)

  implicit lazy val materializer = ActorMaterializer()(context)

  def initializeConsumer: Unit = {

    log.info(s"Initializing consumer for $companyId stock messages with brokers=" + kafkaBrokers)

    implicit val actorSystem = context.system

    val consumerSettings = ConsumerSettings(
      actorSystem, new ByteArrayDeserializer,
      MarketStockMessageDeserializer, Set("StockMessages")
    ).withBootstrapServers(kafkaBrokers)
      .withGroupId(UUID.randomUUID().toString)
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")

    Consumer.committableSource(consumerSettings.withClientId("client1"))
      .runForeach(message => processMessage(message.value))
  }

  def consumeMessage(record: ConsumerRecord[Array[Byte], MarketStockMessage]): ConsumerRecord[Array[Byte], MarketStockMessage] = {
    processMessage(record.value)
    record
  }

  def processMessage(message: MarketStockMessage): Unit = message match {
    case MarketStockMessage(`companyId`, _, price) =>
      if (price.toDouble > stockThreshold)
        log.warning(s"SELL ${companyId} NOW!!!!")
    case _ =>
  }

  override def preStart(): Unit = {
    super.preStart()
    initializeConsumer
  }

  override def receive: Receive = {
    case _ =>
  }
}
