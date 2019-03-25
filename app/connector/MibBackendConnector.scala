package connector

import audit.{ExportAuditData, ImportAuditData}
import javax.inject.{Inject, Singleton}
import model.mib.StoreResponse
import play.api.Configuration
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import uk.gov.hmrc.play.config.ServicesConfig

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MibBackendConnector @Inject() (httpClient: HttpClient, config: MibBackendConnectorConfig)(implicit ec: ExecutionContext) {

  def storeImport(importDec: ImportAuditData)(implicit hc: HeaderCarrier): Future[StoreResponse] = {

    val targetUrl = s"${config.baseUrl}/mib-backend/store/import"
    httpClient.POST[ImportAuditData, StoreResponse](targetUrl, importDec)

  }

  def storeExport(exportDec: ExportAuditData)(implicit hc: HeaderCarrier): Future[StoreResponse] = {

    val targetUrl = s"${config.baseUrl}/mib-backend/store/export"
    httpClient.POST[ExportAuditData, StoreResponse](targetUrl, exportDec)

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
