package audit

import audit.exp.ExportDeclarationCreateAudit
import audit.imp.{ImportDeclarationCreateAudit, PricesTaxesAudit}
import filters.MibSessionIdCheck.sessionIdKey
import model.exp.JourneyDetailsExp
import model.imp.JourneyDetailsImp
import model.shared.MerchandiseDetails
import support.ITSpec
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.logging.RequestId
import uk.gov.hmrc.play.audit.model.ExtendedDataEvent
class AuditorSpec extends ITSpec {

  val auditor = fakeApplication.injector.instanceOf[Auditor]

  "auditData" - {

    "export audit" in {

      val hc = HeaderCarrier()
      val extendedDataEvent: ExtendedDataEvent = auditor.Auditor.getExtendedDataEventExport(TestData.exportData, "merchandiseDeclaration", "session-id-value")(hc)

      extendedDataEvent.auditType shouldBe "merchandiseDeclaration"
      extendedDataEvent.auditSource shouldBe "mib"
      extendedDataEvent.detail.\("submissionRef").get.as[SubmissionRef] shouldBe TestData.submissionRefExport
      extendedDataEvent.detail.\("declarationCreate").get.as[ExportDeclarationCreateAudit] shouldBe TestData.exportDeclarationCreateAudit
      extendedDataEvent.detail.\("journeyDetails").get.as[JourneyDetailsExp] shouldBe TestData.journeyDetailsExp
      extendedDataEvent.detail.\("merchantDetails").get.as[MerchandiseDetails] shouldBe TestData.merchandiseDetails
      extendedDataEvent.detail.\("traderDetails").get.as[TraderDetailsForAudit] shouldBe TestData.traderDetailsUk

    }

    "import audit" in {

      val hc = HeaderCarrier()
      val extendedDataEvent: ExtendedDataEvent = auditor.Auditor.getExtendedDataEventImport(TestData.importData, "merchandiseDeclaration", "session-id-value")(hc)

      extendedDataEvent.auditType shouldBe "merchandiseDeclaration"
      extendedDataEvent.auditSource shouldBe "mib"
      extendedDataEvent.detail.\("submissionRef").get.as[SubmissionRef] shouldBe TestData.submissionRefImport
      extendedDataEvent.detail.\("declarationCreate").get.as[ImportDeclarationCreateAudit] shouldBe TestData.importDeclarationCreateAudit
      extendedDataEvent.detail.\("journeyDetails").get.as[JourneyDetailsImp] shouldBe TestData.journeyDetailsImp
      extendedDataEvent.detail.\("merchantDetails").get.as[MerchandiseDetails] shouldBe TestData.merchandiseDetails
      extendedDataEvent.detail.\("traderDetails").get.as[TraderDetailsForAudit] shouldBe TestData.traderDetailsNonUk
      extendedDataEvent.detail.\("pricesAndTaxes").get.as[PricesTaxesAudit] shouldBe TestData.pricesTaxesAudit

    }

  }

  "auditTags" - {

    "return a map for session information" in {
      val hc = HeaderCarrier(
        trueClientIp = Some("client-ip-value"),
        requestId    = Some(RequestId("request-id-value")),
        deviceID     = Some("device-id-value"),
        otherHeaders = Seq("request.path" -> "/path/payment-id/confirm-payment")).withExtraHeaders(sessionIdKey -> "session-id-value")

      auditor.Auditor.AuditTags(hc).auditTags shouldBe Map(
        "clientIP" -> "client-ip-value",
        "path" -> "/path/payment-id/confirm-payment",
        "X-Request-ID" -> "request-id-value",
        "deviceID" -> "device-id-value")

    }

    "return a map with empty values if session information is missing" in {

      val hc = HeaderCarrier()
      auditor.Auditor.AuditTags(hc).auditTags shouldBe Map(
        "clientIP" -> "-",
        "path" -> "-",
        "X-Request-ID" -> "-",
        "deviceID" -> "-")

    }

  }

}
