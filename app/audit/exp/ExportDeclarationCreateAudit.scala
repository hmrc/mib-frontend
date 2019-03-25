package audit.exp

import model.MibTypes
import play.api.libs.json.{Json, OFormat}

final case class ExportDeclarationCreateAudit(declarationType: String = MibTypes.mibExport.caseValue, purchasePriceInPence: Int, exportDate: String)

object ExportDeclarationCreateAudit {
  implicit val declarationCreateFormat: OFormat[ExportDeclarationCreateAudit] = Json.format[ExportDeclarationCreateAudit]

}
