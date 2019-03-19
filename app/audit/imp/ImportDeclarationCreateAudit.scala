package audit.imp

import play.api.libs.json.{Json, OFormat}

case class ImportDeclarationCreateAudit(declarationType: String = "Import", purchasePriceInPence: Int, importDate: String)

object ImportDeclarationCreateAudit {
  implicit val declarationCreateFormat: OFormat[ImportDeclarationCreateAudit] = Json.format[ImportDeclarationCreateAudit]

}
