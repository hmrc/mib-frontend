package it.testsupport

import akka.actor.ActorSystem
import com.kenshoo.play.metrics.Metrics
import com.typesafe.config.Config
import org.openqa.selenium.WebDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.scalatest._
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.WSClient
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.hooks.HttpHook
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import uk.gov.hmrc.play.http.ws.WSHttp

import scala.concurrent.ExecutionContext

class BrowserSpec
  extends FreeSpec
  with GuiceOneServerPerSuite
  with WireMockSupport
  with RichMatchersIt {

  //TODO: change this to something faster. phantomjs could be an option
  protected implicit val webDriver: WebDriver = new HtmlUnitDriver(false)

  protected val baseUrl = s"http://localhost:$port"

  def goTo(path: String) = webDriver.get(s"$baseUrl$path")

  protected val auditingEnabled: Boolean = false

  implicit lazy val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
  implicit lazy val hc: HeaderCarrier = HeaderCarrier()

  lazy val metrics: Metrics = app.injector.instanceOf[Metrics]

  val httpClient: HttpClient = new HttpClient with WSHttp {
    override def wsClient: WSClient = app.injector.instanceOf(classOf[WSClient])
    override val hooks: Seq[HttpHook] = Nil
    override protected def configuration: Option[Config] = None

    override protected def actorSystem: ActorSystem = ActorSystem()
  }

  override implicit lazy val app: Application = new GuiceApplicationBuilder()
    .configure(
      "microservice.services.pay-api.port" -> WireMockSupport.port,
      "microservice.services.mib-backend.port" -> WireMockSupport.port
    )
    .build()

}
