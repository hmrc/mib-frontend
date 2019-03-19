package audit

import audit.exp.ExportDeclarationCreateAudit
import audit.imp.{ImportDeclarationCreateAudit, PricesTaxesAudit}
import model.exp.JourneyDetailsExp
import model.imp.JourneyDetailsImp
import model.shared.MerchandiseDetails
import play.api.libs.json.{Json, OFormat}

sealed trait AuditData {
}

case class ExportAuditData(submissionRef: SubmissionRef, declarationCreate: ExportDeclarationCreateAudit,
                           journeyDetails: JourneyDetailsExp, merchantDetails: MerchandiseDetails, traderDetails: TraderDetailsForAudit) extends AuditData {

}

object ExportAuditData {

  implicit val exportAuditDataFormat: OFormat[ExportAuditData] = Json.format[ExportAuditData]
}

case class ImportAuditData(submissionRef: SubmissionRef, declarationCreate: ImportDeclarationCreateAudit, pricesAndTaxes: PricesTaxesAudit,
                           journeyDetails: JourneyDetailsImp, merchantDetails: MerchandiseDetails, traderDetails: TraderDetailsForAudit) extends AuditData {

}

object ImportAuditData {

  implicit val importAuditDataFormat: OFormat[ImportAuditData] = Json.format[ImportAuditData]
}

