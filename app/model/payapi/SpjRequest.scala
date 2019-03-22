package model.payapi

import play.api.libs.json.{Json, OFormat, OWrites}

final case class SpjRequest(mibReference: String, vatAmountInPence: Int, dutyAmountInPence: Int, amountInPence: Int, traderDetails: String, merchandiseDetails: String)

object SpjRequest {
  implicit val journeyRequestWrites: OWrites[SpjRequest] = Json.writes[SpjRequest]
  implicit val format: OFormat[SpjRequest] = Json.format[SpjRequest]
}
