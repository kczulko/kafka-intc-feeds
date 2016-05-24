import sbt._

object Dependencies {

  val playJsonVersion = "2.5.2"
  val akkaStreamKafkaVersion = "0.11-M2"

  val playJson = "com.typesafe.play" %% "play-json" % playJsonVersion
  val akkaStreamKafka = "com.typesafe.akka" %% "akka-stream-kafka" % akkaStreamKafkaVersion
  val log4jApi = "org.slf4j" % "slf4j-api" % "1.7.5"
  val log4j12 = "org.slf4j" % "slf4j-log4j12" % "1.7.5"

  val commonDependencies = Seq(
    playJson,
    akkaStreamKafka
  )

  val producerDependencies = Seq(
    log4jApi,
    log4j12,
    akkaStreamKafka
  )

  val consumerDependencies = Seq(
    log4jApi,
    log4j12,
    akkaStreamKafka
  )
}