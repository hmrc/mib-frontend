package model.payapi

import play.api.libs.json.{Json, OFormat, OWrites}

final case class SpjRequest(mibRef: String, amountInPence: Int, returnUrl: String = "http://www.gov.uk",
                            backUrl: String = "http://www.gov.uk", traderAddress: String, descriptionMerchandise: String)

object SpjRequest {
  implicit val journeyRequestWrites: OWrites[SpjRequest] = Json.writes[SpjRequest]
  implicit val format: OFormat[SpjRequest] = Json.format[SpjRequest]
}
