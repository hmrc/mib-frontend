package audit.imp

import model.MibTypes
import play.api.libs.json.{Json, OFormat}

final case class ImportDeclarationCreateAudit(declarationType: String = MibTypes.mibImport.caseValue, purchasePriceInPence: Int, importDate: String)

object ImportDeclarationCreateAudit {
  implicit val declarationCreateFormat: OFormat[ImportDeclarationCreateAudit] = Json.format[ImportDeclarationCreateAudit]

}
