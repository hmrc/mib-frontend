package audit

import filters.MibSessionIdCheck.sessionIdKey
import javax.inject.{Inject, Singleton}
import model.{MibType, MibTypes}
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, Request}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.audit.model.ExtendedDataEvent

import scala.concurrent.ExecutionContext

@Singleton
class Auditor @Inject() (auditConnector: AuditConnector) {

  import Auditor._

  private implicit val logger: Logger = Logger(this.getClass)

  def sendAuditData(auditData: AuditData, mibType: MibType, eventName: String)(implicit hc: HeaderCarrier, ec: ExecutionContext, request: Request[AnyContent]) = {
    val sessionIdValue = request.headers.get(sessionIdKey).getOrElse("-")
    val extendedDataEvent: ExtendedDataEvent = {
      if (mibType == MibTypes.mibExport) getExtendedDataEventExport(auditData.asInstanceOf[ExportAuditData], eventName, sessionIdValue)(hc)
      else
        getExtendedDataEventImport(auditData.asInstanceOf[ImportAuditData], eventName, sessionIdValue)(hc)
    }

    val eventualResult = auditConnector.sendExtendedEvent(extendedDataEvent)

    eventualResult.onFailure {
      case ex: Throwable => logger.warn(s"MIBAuditEvent:: Failed with exception: ${ex.getMessage}", ex)
    }
    eventualResult

  }

  object Auditor {

    implicit class AuditTags(hc: HeaderCarrier) {

      def auditTags = Map(
        "path" -> hc.otherHeaders.find(p => p._1.equals("request.path")).getOrElse("-" -> "-")._2,
        "clientIP" -> hc.trueClientIp.getOrElse("-"),
        "X-Request-ID" -> hc.requestId.map(_.value).getOrElse("-"),
        "deviceID" -> hc.deviceID.getOrElse("-"))
    }

    def getExtendedDataEventExport(auditData: ExportAuditData, eventName: String, sessionId: String)(implicit hc: HeaderCarrier): ExtendedDataEvent = {

      ExtendedDataEvent(auditSource = "mib", auditType = eventName, tags = hc.auditTags + ("X-Session-ID" -> sessionId), detail = Json.toJson(auditData))
    }

    def getExtendedDataEventImport(auditData: ImportAuditData, eventName: String, sessionId: String)(implicit hc: HeaderCarrier): ExtendedDataEvent = {

      ExtendedDataEvent(auditSource = "mib", auditType = eventName, tags = hc.auditTags + ("X-Session-ID" -> sessionId), detail = Json.toJson(auditData))
    }

  }

}
