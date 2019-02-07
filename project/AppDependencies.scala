import play.core.PlayVersion
import sbt._

object AppDependencies {

  val compile = Seq(

    "uk.gov.hmrc" %% "govuk-template" % "5.27.0-play-25",
    "uk.gov.hmrc" %% "play-ui" % "7.30.0-play-25",
    "uk.gov.hmrc" %% "bootstrap-play-25" % "4.7.0"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "hmrctest" % "3.3.0",
    "org.scalatest" %% "scalatest" % "3.0.5",
    "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0", //watch out version, 3.xx introduces play 2.6
    "org.pegdown" % "pegdown" % "1.6.0",
    "org.mockito" % "mockito-core" % "2.18.3",
    "com.github.tomakehurst" % "wiremock" % "2.17.0",
    "com.typesafe.play" %% "play-test" % PlayVersion.current
  )

}
