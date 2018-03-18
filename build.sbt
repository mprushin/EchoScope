name := "EchoScope"

version := "0.1"

scalaVersion := "2.12.4"

val circeVersion = "0.9.1"

libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.3.0"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.1.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies ++= Seq(
  "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.16",
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.0")