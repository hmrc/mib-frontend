package it.tests

import it.pages._
import it.testdata.TestData.{purchasePriceExample, _}
import it.testsupport.BrowserSpec
import org.openqa.selenium.WebDriver

class JourneyDetailsPageSpec extends BrowserSpec {

  "show the journey details page for export" in new SetUp {
    goToPageVia(asImport = false)
    JourneyDetailsPage.assertExportPageIsDisplayed()
  }

  "show the journey details page for import" in new SetUp {
    goToPageVia()
    JourneyDetailsPage.assertImportPageIsDisplayed()
  }

  "show an error if nothing is entered " in new SetUp {
    goToPageVia(asImport = false)
    JourneyDetailsPage.clickContinue()
    JourneyDetailsPage.assertErrorSummaryIsShown()
  }
  "show the next page for export " in new SetUp {
    goToPageVia(asImport = false)
    JourneyDetailsPage.enterDetailsExport(testPortOfExit, None, testCountry)
    JourneyDetailsPage.clickContinue()
    DetailsOfTraderPage.assertExportPageIsDisplayed()
  }

  "show the next page for import " in new SetUp {
    goToPageVia()
    JourneyDetailsPage.enterDetailsImport(testPortOfEntry, testEoriNumber, testCountry)
    JourneyDetailsPage.clickContinue()
    DetailsOfTraderPage.assertImportPageIsDisplayed()
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
      }
    }
  }

}
