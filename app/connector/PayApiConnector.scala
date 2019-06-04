package connector

import javax.inject.{Inject, Singleton}
import model.payapi.{SpjRequest, SpjResponse}
import play.api.Configuration
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PayApiConnector @Inject() (httpClient: HttpClient, config: PayApiConnectorConfig)(implicit ec: ExecutionContext) {

  def createJourney(journeyRequest: SpjRequest)(implicit hc: HeaderCarrier): Future[SpjResponse] = {

    val targetUrl = s"${config.baseUrl}/pay-api/mib-frontend/mib/journey/start"
    httpClient.POST[SpjRequest, SpjResponse](targetUrl, journeyRequest)

  }

}

final case class PayApiConnectorConfig(
    baseUrl: String) {

  @Inject()
  def this(sConfig: ServicesConfig, configuration: Configuration) {
    this(
      baseUrl = sConfig.baseUrl("pay-api"))
  }
}
