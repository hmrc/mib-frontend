package audit.exp

import play.api.libs.json.{Json, OFormat}

case class ExportDeclarationCreateAudit(declarationType: String = "Export", purchasePriceInPence: Int, exportDate: String)

object ExportDeclarationCreateAudit {
  implicit val declarationCreateFormat: OFormat[ExportDeclarationCreateAudit] = Json.format[ExportDeclarationCreateAudit]

}
