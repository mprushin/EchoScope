import sbt.Keys.libraryDependencies

val circeVersion = "0.9.1"

lazy val commonSettings = Seq(
  version := "0.1",
  scalaVersion := "2.11.7",
  //ScalaTest
  libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % "3.0.4",
    "org.scalatest" %% "scalatest" % "3.0.4" % "test",
    "junit" % "junit" % "4.10" % Test,
  ),

  //AWS
  libraryDependencies ++= Seq(
    "com.amazonaws" % "aws-java-sdk-s3" % "1.11.0"
  ),

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


)

lazy val loaderSettings = Seq(

  //Java Libs
  libraryDependencies ++= Seq(
    "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.16"
  )

)

lazy val analyticsSettings = Seq(

  libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % "2.3.0"
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

lazy val analytics = (project in file("analytics"))
  .settings(
    name := "EchoScope Analytics",
    commonSettings,
    analyticsSettings
  ).dependsOn(core)