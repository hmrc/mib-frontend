package audit

import audit.exp.ExportDeclarationCreateAudit
import audit.imp.{ImportDeclarationCreateAudit, PricesTaxesAudit}
import model.exp.JourneyDetailsExp
import model.imp.JourneyDetailsImp
import model.shared.MerchandiseDetails

object TestData {

  val submissionRefExport: SubmissionRef = new SubmissionRef("MIBE1234567890")
  val exportDeclarationCreateAudit: ExportDeclarationCreateAudit = new ExportDeclarationCreateAudit(purchasePriceInPence = 200, exportDate = "20-03-2019")
  val journeyDetailsExp: JourneyDetailsExp = new JourneyDetailsExp("port of exit", Some("Eori"), "destination country")

  val submissionRefImport: SubmissionRef = new SubmissionRef("MIBI1234567890")
  val importDeclarationCreateAudit: ImportDeclarationCreateAudit = new ImportDeclarationCreateAudit(purchasePriceInPence = 200, importDate = "20-03-2019")
  val journeyDetailsImp: JourneyDetailsImp = new JourneyDetailsImp("port of entry", "country of origin", Some("eori"))
  val pricesTaxesAudit: PricesTaxesAudit = new PricesTaxesAudit(50, 20)

  val merchandiseDetails: MerchandiseDetails = new MerchandiseDetails(Some("invoice number"), "description of goods")

  val ukAddress = UkAddress("building and street", Some("line2"), Some("city"), Some("county"), "bn12 4xl")
  val traderDetailsUk: TraderDetailsForAudit = new TraderDetailsForAudit(NameOfTrader("name of trader"), Some(ukAddress), None, Some("vrn"), Some("Vehicle Reg"))

  val nonUkAddress = NonUkAddress("line1", Some("line2"), Some("line3"), "Country")
  val traderDetailsNonUk: TraderDetailsForAudit = new TraderDetailsForAudit(NameOfTrader("name of trader"), None, Some(nonUkAddress), Some("vrn"), Some("Vehicle Reg"))

  val exportData: ExportAuditData = new ExportAuditData(submissionRefExport, exportDeclarationCreateAudit, journeyDetailsExp, merchandiseDetails, traderDetailsUk)
  val importData: ImportAuditData = new ImportAuditData(submissionRefImport, importDeclarationCreateAudit, pricesTaxesAudit,
                                                        journeyDetailsImp, merchandiseDetails, traderDetailsNonUk)

}
