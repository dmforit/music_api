import play.sbt.PlayScala
import play.sbt.PlayImport.guice

name := """api_music"""
organization := "com.api_music"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.17"

libraryDependencies += guice
libraryDependencies += "org.sangria-graphql" %% "sangria" % "3.5.3"
libraryDependencies += "org.sangria-graphql" %% "sangria-play-json" % "2.0.2"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "5.1.0",
  "org.postgresql" % "postgresql" % "42.5.4",
)
libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.4.1",
  "org.postgresql" % "postgresql" % "42.5.4" //org.postgresql.ds.PGSimpleDataSource dependency
)


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
