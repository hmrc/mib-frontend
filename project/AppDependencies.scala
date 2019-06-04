import play.core.PlayVersion
import sbt._

object AppDependencies {
  
  val compile = Seq(

    "uk.gov.hmrc" %% "govuk-template" % "5.35.0-play-26",
    "uk.gov.hmrc" %% "play-ui" % "7.40.0-play-26",
    "uk.gov.hmrc" %% "bootstrap-play-26"       % "0.40.0",
    "com.beachape" %% "enumeratum" % "1.5.13",
    "uk.gov.hmrc" %% "time" % "3.3.0",
    "org.xhtmlrenderer" % "core-renderer" % "R8" exclude("bouncycastle", "bcprov-jdk14"),
    "com.typesafe.play" %% "play-json-joda" % "2.6.10"
  )

  val test = Seq(
    "org.scalatest" %% "scalatest" % "3.0.5",
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2", 
    "org.pegdown" % "pegdown" % "1.6.0",
    "org.mockito" % "mockito-core" % "2.23.0",
    "com.github.tomakehurst" % "wiremock-jre8" % "2.21.0",
    "com.typesafe.play" %% "play-test" % PlayVersion.current
  )

}
