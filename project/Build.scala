import Dependencies.{commonDependencies, consumerDependencies, producerDependencies}
//import com.typesafe.sbt.SbtStartScript
import sbt.Keys._
import sbt._

object KafkaIntcFeedsBuild extends Build {

  val globalScalaVersion = "2.11.8"

  lazy val common = Project(
    id = "common",
    base = file("common"),
    settings = Seq(
      scalaVersion := globalScalaVersion,
      libraryDependencies ++= commonDependencies
    )
  )

  lazy val producer = Project(
    id = "producer",
    base = file("producer"),
    settings = Seq(
      scalaVersion := globalScalaVersion,
      libraryDependencies ++= producerDependencies
    )
  ).dependsOn(common)

  lazy val consumer = Project(
      id = "consumer",
      base = file("consumer"),
      settings = Seq(
        scalaVersion := globalScalaVersion,
        libraryDependencies ++= consumerDependencies
      )
    ).dependsOn(common)
}