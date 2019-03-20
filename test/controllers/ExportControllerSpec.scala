package controllers

import exceptions.MibException
import model.ExportPages.{export_date, prices, trader_details}
import model.ImportPages.journey_details
import model.{ExportPages, MibTypes}
import model.TestData.{testImportExportDate, testPrices, _}
import model.exp.JourneyDetailsExp
import model.shared.{ImportExportDate, MerchandiseDetails, Prices, TraderDetails}
import org.scalatest.concurrent.ScalaFutures
import play.api.http.Status._
import play.api.test.FakeRequest
import support.ITSpec
import model.TestData.testTraderDetails
class ExportControllerSpec extends ITSpec with ScalaFutures {

  val ExportControllerSpec = fakeApplication.injector.instanceOf[ExportController]
  "ExportController" - {
    "should return an BadRequest if the page is not found" in {
      ExportControllerSpec.getExportPage("pageDoesNotExist")(FakeRequest()).futureValue.header.status shouldBe BAD_REQUEST
    }

    "should return an exception if purchase_prices is not in the session" in {
      intercept[MibException] { ExportControllerSpec.getExportPage(prices.case_value)(FakeRequest()) }.message shouldBe "Prices details not found"
    }

    "should return an ok if it is in the session for prices" in {
      ExportControllerSpec.getExportPage(prices.case_value)(FakeRequest().withSession(Prices.toSession(testPrices, MibTypes.mibExport): _*)).futureValue.header.status shouldBe OK
    }

    "should return an ok if it is in the session for export_date" in {
      ExportControllerSpec.getExportPage(export_date.case_value)(FakeRequest().withSession(ImportExportDate.toSession(testImportExportDate, MibTypes.mibExport): _*)).futureValue.header.status shouldBe OK
    }

    "should return an ok if it is in the session for journey_details" in {
      ExportControllerSpec.getExportPage(journey_details.case_value)(FakeRequest().withSession(JourneyDetailsExp.toSession(testJourneyExport): _*)).futureValue.header.status shouldBe OK
    }

    "should return an exception if journey_details is not in the session" in {
      intercept[MibException] { ExportControllerSpec.getExportPage(journey_details.case_value)(FakeRequest()) }.message shouldBe "Journey Details not found"
    }

    "should return an exception if trader_details is not in the session" in {
      intercept[MibException] { ExportControllerSpec.getExportPage(trader_details.case_value)(FakeRequest()) }.message shouldBe "Trader Details not found"
    }
    "should return an ok if it is in the session for trader_details" in {
      ExportControllerSpec.getExportPage(trader_details.case_value)(FakeRequest().withSession(TraderDetails.toSession(testTraderDetails, MibTypes.mibExport): _*)).futureValue.header.status shouldBe OK
    }
    "should return an exception if merchandise_details is not in the session" in {
      intercept[MibException] { ExportControllerSpec.getExportPage(ExportPages.merchandise_details.case_value)(FakeRequest()) }.message shouldBe "Merchandise Details not found"
    }
    "should return an ok if merchandise_details is not in the session" in {
      ExportControllerSpec.getExportPage(ExportPages.merchandise_details.case_value)(FakeRequest().withSession(MerchandiseDetails.toSession(testMerchantDetails, MibTypes.mibExport): _*)).futureValue.header.status shouldBe OK
    }

    "should return an ok if check_details is not in the session" in {
      val totalSession =
        Prices.toSession(testPrices, MibTypes.mibExport) ++
          ImportExportDate.toSession(testImportExportDate, MibTypes.mibExport) ++
          JourneyDetailsExp.toSession(testJourneyExport) ++
          TraderDetails.toSession(testTraderDetails, MibTypes.mibExport) ++
          MerchandiseDetails.toSession(testMerchantDetails, MibTypes.mibExport)
      ExportControllerSpec.getExportPage(ExportPages.check_details.case_value)(FakeRequest().withSession(totalSession: _*)).futureValue.header.status shouldBe OK
    }
  }
}
