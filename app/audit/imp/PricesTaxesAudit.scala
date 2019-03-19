package audit.imp

import play.api.libs.json.{Json, OFormat}

case class PricesTaxesAudit(customsDutyInPence: Int, importVatInPence: Int, totalTaxDue: Int) {
  def this(customsDutyInPence: Int, importVatInPence: Int) = this(customsDutyInPence, importVatInPence, customsDutyInPence + importVatInPence)
}

object PricesTaxesAudit {

  implicit val PricesTaxesAudit: OFormat[PricesTaxesAudit] = Json.format[PricesTaxesAudit]
}

