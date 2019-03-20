package model

import model.exp.{DeclarationReceived, JourneyDetailsExp}
import model.imp.{JourneyDetailsImp, PricesTaxesImp}
import model.shared.{ImportExportDate, MerchandiseDetails, Prices, TraderDetails}
object TestData {

  val testDeclaration = DeclarationReceived("currentDate", "traderNameAndAddress", "description", "mibReference")

  val testPrices = Prices(123)

  val testPricesImp = PricesTaxesImp(500, 250)

  val testImportExportDate = ImportExportDate(22, 1, 1989)

  val testJourneyExport = JourneyDetailsExp("brexit", None, "Ireland")

  val testJourneyImport = JourneyDetailsImp("brexit", "Ireland", Some("Ireland"))

  val testTraderDetails = TraderDetails("Yes", "colm", None, None, None, None, None, None, None, None, None, None, None)

  val testMerchantDetails = MerchandiseDetails(None, "Goods")
}
