ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "Spotify"
  )
libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.4.2"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.2"
