package connector

import javax.inject.{Inject, Singleton}
import play.api.libs.ws.{WSClient, WSResponse}
import uk.gov.hmrc.play.config.ServicesConfig

import scala.concurrent.Future

@Singleton
class PdfGeneratorConnector @Inject() (serviceConfig: ServicesConfig)(ws: WSClient) {
  val serviceURL = s"""${serviceConfig.baseUrl("pdf-generator-service")}/pdf-generator-service/generate"""

  def generatePdf(html: String): Future[WSResponse] = {
    ws.url(serviceURL).post(Map("html" -> Seq(html)))
  }
}
