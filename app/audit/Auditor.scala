package audit

import javax.inject.{Inject, Singleton}
import model.{MibType, MibTypes}
import play.api.Logger
import play.api.libs.json.Json
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.audit.model.ExtendedDataEvent

import scala.concurrent.ExecutionContext

@Singleton
class Auditor @Inject() (auditConnector: AuditConnector) {

  private implicit val logger: Logger = Logger(this.getClass)

  def apply(auditData: AuditData, mibType: MibType, eventName: String)(implicit hc: HeaderCarrier, ec: ExecutionContext) = {

    val extendedDataEvent: ExtendedDataEvent = {
      if (mibType == MibTypes.mibExport) Auditor.getExtendedDataEventExport(auditData.asInstanceOf[ExportAuditData], eventName)(hc)
      else
        Auditor.getExtendedDataEventImport(auditData.asInstanceOf[ImportAuditData], eventName)(hc)
    }

    val eventualResult = auditConnector.sendExtendedEvent(extendedDataEvent)

    eventualResult.onFailure {
      case ex: Throwable => logger.warn(s"PaymentAuditEvent:: Failed with exception: ${ex.getMessage}", ex)
    }
    eventualResult

  }

  object Auditor {

    def getExtendedDataEventExport(auditData: ExportAuditData, eventName: String)(implicit hc: HeaderCarrier): ExtendedDataEvent = {

      ExtendedDataEvent(auditSource = "mib", auditType = eventName, tags = hc.auditTags, detail = Json.toJson(auditData))
    }

    def getExtendedDataEventImport(auditData: ImportAuditData, eventName: String)(implicit hc: HeaderCarrier): ExtendedDataEvent = {

      ExtendedDataEvent(auditSource = "mib", auditType = eventName, tags = hc.auditTags, detail = Json.toJson(auditData))
    }

    implicit class AuditTags(hc: HeaderCarrier) {
      def auditTags = Map(
        "path" -> hc.otherHeaders.find(p => p._1.equals("request.path")).getOrElse("-" -> "-")._2,
        "clientIP" -> hc.trueClientIp.getOrElse("-"),
        "clientIP" -> hc.trueClientIp.getOrElse("-"),
        "X-Request-Chain" -> hc.requestChain.value,
        "X-Session-ID" -> hc.sessionId.map(_.value).getOrElse("-"),
        "X-Request-ID" -> hc.requestId.map(_.value).getOrElse("-"),
        "deviceID" -> hc.deviceID.getOrElse("-"))
    }

  }
}
