package it.tests

import it.pages._
import it.testdata.TestData.{testPortOfEntry, purchasePriceExample, testUkTraderDetails}
import it.testsupport.BrowserSpec
import org.openqa.selenium.WebDriver
import it.testdata.TestData._
import it.pages.CheckDetailsPage
class MerchandiseDetailsPageSpec extends BrowserSpec {

  "show the Merchandise Details  page export" in new SetUp {
    goToPageVia(asImport = false)
    MerchandiseDetailsPage.assertExportPageIsDisplayed()
  }
  "show the Merchandise Details  page import" in new SetUp {
    goToPageVia()
    MerchandiseDetailsPage.assertImportPageIsDisplayed()
  }
  "show an error if nothing is entered " in new SetUp {
    goToPageVia(asImport = false)
    MerchandiseDetailsPage.assertExportPageIsDisplayed()
    MerchandiseDetailsPage.clickContinue()
    MerchandiseDetailsPage.assertExportPageIsDisplayed()
  }

  "go to the next page export" in new SetUp {
    goToPageVia(asImport = false)
    MerchandiseDetailsPage.enterValue(Some(testInvoiceNumber), testDescriptionOfGoods)
    MerchandiseDetailsPage.clickContinue()
    CheckDetailsPage.assertExportPageIsDisplayed()
  }

  trait SetUp {
    def goToPageVia(asImport: Boolean = true)(implicit webDriver: WebDriver): Unit = {
      goTo(SelectDeclarationPage.path)
      if (asImport) SelectDeclarationPage.clickImport() else SelectDeclarationPage.clickExport()
      SelectDeclarationPage.clickContinue()
      EnterPurchasePricePage.enterIntoPurchasePrice(purchasePriceExample)
      EnterPurchasePricePage.clickContinue()
      EnterDatePage.enterDate()
      EnterDatePage.clickContinue()
      if (asImport) {
        PricesAndTaxesImportPage.enterDetails(testCustomsDuty, testImportVat)
        PricesAndTaxesImportPage.clickContinue()
        TaxDuePage.clickContinue()
        JourneyDetailsPage.enterDetailsImport(testPortOfEntry, testEoriNumber, testCountry)
        JourneyDetailsPage.clickContinue()
      } else {
        JourneyDetailsPage.enterDetailsExport(testPortOfEntry)
        JourneyDetailsPage.clickContinue()
      }
      DetailsOfTraderPage.enterDetails(testUkTraderDetails)
      DetailsOfTraderPage.clickContinue()
    }
  }
}

