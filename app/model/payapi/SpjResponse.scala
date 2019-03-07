package model.payapi

import play.api.libs.json.{Json, Reads}

case class SpjResponse(nextUrl: String, journeyId: String)

object SpjResponse {
  implicit val reads: Reads[SpjResponse] = Json.reads
}
