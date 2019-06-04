package connector

import audit.{ExportAuditData, ImportAuditData}
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaStreamAudioSourceNode
import javax.inject.{Inject, Singleton}
import model.mib.StoreResponse
import play.Logger
import play.api.Configuration
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import play.mvc.Http.Status
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MibBackendConnector @Inject() (httpClient: HttpClient, config: MibBackendConnectorConfig)(implicit ec: ExecutionContext) {

  def storeImport(importDec: ImportAuditData)(implicit hc: HeaderCarrier): Future[StoreResponse] = {

    val targetUrl = s"${config.baseUrl}/mib-backend/store/import"
    httpClient.POST(targetUrl, importDec).map(res => res.status match {
      case Status.CREATED => res.json.as[StoreResponse]

    }
    )
  }

  def storeExport(exportDec: ExportAuditData)(implicit hc: HeaderCarrier): Future[StoreResponse] = {

    val targetUrl = s"${config.baseUrl}/mib-backend/store/export"
    httpClient.POST(targetUrl, exportDec).map(res => res.status match {
      case Status.CREATED => res.json.as[StoreResponse]
      case _              => Logger.debug("AAAAA did not store"); throw new RuntimeException("did not store")
    }
    )

  }

}

final case class MibBackendConnectorConfig(
    baseUrl: String) {

  @Inject()
  def this(sConfig: ServicesConfig, configuration: Configuration) {
    this(
      baseUrl = sConfig.baseUrl("mib-backend"))
  }
}

final case object MibResponse
