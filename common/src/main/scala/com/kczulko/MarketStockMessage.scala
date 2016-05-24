package com.kczulko

import java.util
import play.api.libs.json._

import org.apache.kafka.common.serialization.{Deserializer, Serializer}

case class MarketStockMessage(companyId: String, time: String, stockValue: Double)

object MarketStockMessageSerializer extends Serializer[MarketStockMessage] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def serialize(topic: String, data: MarketStockMessage): Array[Byte] = {
    Json.writes[MarketStockMessage].writes(data).toString getBytes("UTF8")
  }

  override def close(): Unit = {}
}

object MarketStockMessageDeserializer extends Deserializer[MarketStockMessage ] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def close(): Unit = {}

  override def deserialize(topic: String, data: Array[Byte]): MarketStockMessage = {
    val json: JsValue = Json.parse(new String(data, "UTF8"))
    Json.format[MarketStockMessage].reads(json).asOpt match {
      case Some(message) => message
      case None => throw new IllegalStateException(s"Cannot deserialize message: {$json}")
    }
  }
}