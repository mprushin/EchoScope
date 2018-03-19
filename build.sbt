val circeVersion = "0.9.1"

lazy val commonSettings = Seq(
  version := "0.1",
  scalaVersion := "2.12.4")

lazy val loaderSettings = Seq(
  //Core
  libraryDependencies ++= Seq(
    "org.scalaj" %% "scalaj-http" % "2.3.0",
    "org.scala-lang.modules" %% "scala-xml" % "1.1.0"
  ),

  //Circe
  libraryDependencies ++= Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeVersion),

  //Java Libs
  libraryDependencies ++= Seq(
    "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.16",
    "com.amazonaws" % "aws-java-sdk-s3" % "1.11.0"),

  //ScalaTest
  libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % "3.0.4",
    "org.scalatest" %% "scalatest" % "3.0.4" % "test",
    "junit" % "junit" % "4.10" % Test,

  )
)

lazy val core = (project in file("core"))
  .settings(
    name := "EchoScope Core",
    commonSettings
  )

lazy val loader = (project in file("loader"))
  .settings(
    name := "EchoScope Loader",
    commonSettings,
    loaderSettings
  ).dependsOn(core)
