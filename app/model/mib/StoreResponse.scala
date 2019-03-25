package model.mib

import play.api.libs.functional.syntax._
import play.api.libs.json._

final case class StoreResponse(value: String)

object StoreResponse {

  implicit val format: Format[StoreResponse] = implicitly[Format[String]].inmap(StoreResponse(_), _.value)

}
