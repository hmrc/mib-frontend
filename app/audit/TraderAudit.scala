package audit

import play.api.libs.json.{Format, Json, OFormat}
import play.api.libs.functional.syntax._

final case class NameOfTrader(value: String)

object NameOfTrader {
  implicit val format: Format[NameOfTrader] = implicitly[Format[String]].inmap(NameOfTrader(_), _.value)
}

final case class UkAddress(buildingAndStreet: String, line2: Option[String], city: Option[String], county: Option[String], postcode: String)
object UkAddress {
  implicit val ukAddressFormat: OFormat[UkAddress] = Json.format[UkAddress]
}

final case class NonUkAddress(line1: String, line2: Option[String], line3: Option[String], country: String)
object NonUkAddress {
  implicit val nonUkAddressFormat: OFormat[NonUkAddress] = Json.format[NonUkAddress]
}

final case class TraderDetailsForAudit(nameOfTrader: NameOfTrader, businessAddressUk: Option[UkAddress], businessAddressNonUk: Option[NonUkAddress], vrn: Option[String],
                                       vehicleRegNo: Option[String])

object TraderDetailsForAudit {
  implicit val traderDetailsForAuditFormat: OFormat[TraderDetailsForAudit] = Json.format[TraderDetailsForAudit]
}

