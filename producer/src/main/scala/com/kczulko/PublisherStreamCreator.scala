package com.kczulko

import java.lang.System.getProperty

import akka.kafka.scaladsl._
import akka.actor.{Actor, ActorLogging, Props}
import akka.kafka.ProducerSettings
import akka.stream._
import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.ByteArraySerializer
import org.reactivestreams.Publisher

private class PublisherStreamCreator extends Actor with ActorLogging {

  implicit lazy val materializer = ActorMaterializer()(context)

  lazy val kafkaBrokers = Option(getProperty("brokers")).getOrElse("localhost:9092")

  override def preStart(): Unit = {
    super.preStart()
    initializePublisher
  }

  override def receive: Receive = {
    case _ =>
  }

  def initializePublisher = {
    log.info("Initializing publisher with brokers:" + kafkaBrokers)

    val consumerSettings = ProducerSettings(
      context.system,
      new ByteArraySerializer,
      MarketStockMessageSerializer
    ).withBootstrapServers(kafkaBrokers)
     .withProperty(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, "5000")

    val producer = context.actorOf(Props(new IntcFeedsProducer))
    val publisher: Publisher[MarketStockMessage] = ActorPublisher[MarketStockMessage](producer)

    Source.fromPublisher(publisher)
      .map(new ProducerRecord[Array[Byte], MarketStockMessage]("StockMessages", _))
      .to(Producer.plainSink(consumerSettings)).run()
  }
}
