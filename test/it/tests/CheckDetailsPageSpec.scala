package it.tests

import it.pages._
import it.testdata.TestData._
import it.testsupport.BrowserSpec
import org.openqa.selenium.WebDriver
import it.pages.DeclarationPage
import it.pages.DeclarationPage.currentPath
class CheckDetailsPageSpec extends BrowserSpec {

  "show the Details of trader page export" in new SetUp {
    goToPageVia(asImport = false)
    CheckDetailsPage.assertExportPageIsDisplayed()
  }

  "show the Details of trader page import" in new SetUp {
    goToPageVia()
    CheckDetailsPage.assertImportPageIsDisplayed()
  }

  "go to the next page for export " in new SetUp {
    goToPageVia(asImport = false)
    CheckDetailsPage.clickContinue()
    DeclarationPage.assertPageIsDisplayed()
  }

  "go start the payments journey " in new SetUp {
    goToPageVia()
    CheckDetailsPage.clickContinue()
    currentPath shouldBe "/mib-frontend/start-journey"
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
        JourneyDetailsPage.enterDetailsExport(testPortOfExit, None, testCountry)
        JourneyDetailsPage.clickContinue()
      }
      DetailsOfTraderPage.enterDetails(testUkTraderDetails)
      DetailsOfTraderPage.clickContinue()
      MerchandiseDetailsPage.enterValue(Some(testInvoiceNumber), testDescriptionOfGoods)
      MerchandiseDetailsPage.clickContinue()
    }
  }
}
