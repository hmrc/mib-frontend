package controllers

import exceptions.MibException
import model.ExportPages.{prices, trader_details}
import model.ImportPages.{import_date, journey_details, prices_taxes}
import model.TestData._
import model.imp.{JourneyDetailsImp, PricesTaxesImp}
import model.shared.{ImportExportDate, MerchandiseDetails, Prices, TraderDetails}
import model.{ExportPages, MibTypes}
import org.scalatest.concurrent.ScalaFutures
import play.api.http.Status._
import play.api.test.FakeRequest
import support.ITSpec
class ImportControllerSpec extends ITSpec with ScalaFutures {

  val ImportController = fakeApplication.injector.instanceOf[ImportController]
  "ImportController" - {
    "should return an BadRequest if the page is not found" in {
      ImportController.getImportPage("pageDoesNotExist")(FakeRequest()).futureValue.header.status shouldBe BAD_REQUEST
    }

    "should return an exception if purchase_prices is not in the session" in {
      intercept[MibException] { ImportController.getImportPage(prices.case_value)(FakeRequest()) }.message shouldBe "Prices Details not found"
    }

    "should return an exception if import_date is not in the session" in {
      intercept[MibException] { ImportController.getImportPage(import_date.case_value)(FakeRequest()) }.message shouldBe "ImportExport date details not found"
    }
    "should return an ok if it is in the session for import_date" in {
      ImportController.getImportPage(import_date.case_value)(FakeRequest().withSession(ImportExportDate.toSession(testImportExportDate, MibTypes.mibImport): _*)).futureValue.header.status shouldBe OK
    }

    "should return an ok if it is in the session for journey_details" in {
      ImportController.getImportPage(journey_details.case_value)(FakeRequest().withSession(JourneyDetailsImp.toSession(testJourneyImport): _*)).futureValue.header.status shouldBe OK
    }

    "should return an ok if prices_taxes is in the session" in {
      ImportController.getImportPage(prices_taxes.case_value)(FakeRequest().withSession(Prices.toSession(testPrices, MibTypes.mibImport) ++ PricesTaxesImp.toSession(testPricesImp): _*)).futureValue.header.status shouldBe OK
    }

    "should return an exception if journey_details is not in the session" in {
      intercept[MibException] { ImportController.getImportPage(journey_details.case_value)(FakeRequest().withSession(PricesTaxesImp.toSession(testPricesImp): _*)) }.message shouldBe "Journey Details not found"
    }

    "should return an exception if trader_details is not in the session" in {
      intercept[MibException] { ImportController.getImportPage(trader_details.case_value)(FakeRequest()) }.message shouldBe "Trader Details not found"
    }
    "should return an ok if it is in the session for trader_details" in {
      ImportController.getImportPage(trader_details.case_value)(FakeRequest().withSession(TraderDetails.toSession(testTraderDetails, MibTypes.mibImport): _*)).futureValue.header.status shouldBe OK
    }
    "should return an exception if merchandise_details is not in the session" in {
      intercept[MibException] { ImportController.getImportPage(ExportPages.merchandise_details.case_value)(FakeRequest()) }.message shouldBe "Merchandise Details not found"
    }
    "should return an ok if merchandise_details is not in the session" in {
      ImportController.getImportPage(ExportPages.merchandise_details.case_value)(FakeRequest().withSession(MerchandiseDetails.toSession(testMerchantDetails, MibTypes.mibImport): _*)).futureValue.header.status shouldBe OK
    }

    "should return an ok if check_details is not in the session" in {
      val totalSession =
        Prices.toSession(testPrices, MibTypes.mibImport) ++
          PricesTaxesImp.toSession(testPricesImp) ++
          ImportExportDate.toSession(testImportExportDate, MibTypes.mibImport) ++
          JourneyDetailsImp.toSession(testJourneyImport) ++
          TraderDetails.toSession(testTraderDetails, MibTypes.mibImport) ++
          MerchandiseDetails.toSession(testMerchantDetails, MibTypes.mibImport)
      ImportController.getImportPage(ExportPages.check_details.case_value)(FakeRequest().withSession(totalSession: _*)).futureValue.header.status shouldBe OK
    }
  }
}
