package model.payapi

import play.api.libs.json.{Json, Reads}

final case class SpjResponse(nextUrl: String, journeyId: String)

object SpjResponse {
  implicit val reads: Reads[SpjResponse] = Json.reads
}
