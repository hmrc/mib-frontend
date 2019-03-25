package audit.imp

import play.api.libs.json.{Json, OFormat}

final case class PricesTaxesAudit(customsDutyInPence: Int, importVatInPence: Int, totalTaxDue: Int)

object PricesTaxesAudit {

  implicit val PricesTaxesAudit: OFormat[PricesTaxesAudit] = Json.format[PricesTaxesAudit]
}

