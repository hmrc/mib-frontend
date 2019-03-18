package it.tests

import it.pages._
import it.testdata.TestData.{testPortOfEntry, purchasePriceExample}
import it.testsupport.BrowserSpec
import org.openqa.selenium.WebDriver
import it.testdata.TestData.testUkTraderDetails
class DetailsOfTraderPageSpec extends BrowserSpec {

  "show the Details of trader page " in new SetUp {
    goToPageVia(asImport = false)
    DetailsOfTraderPage.assertExportPageIsDisplayed()
  }

  "show an error if noting is entered " in new SetUp {
    goToPageVia(asImport = false)
    DetailsOfTraderPage.clickContinue()
    DetailsOfTraderPage.assertErrorSummaryIsShown()
  }
  "show the next page " in new SetUp {
    goToPageVia(asImport = false)
    DetailsOfTraderPage.enterDetails(testUkTraderDetails)
    DetailsOfTraderPage.clickContinue()
    MerchandiseDetailsPage.assertExportPageIsDisplayed()
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
      JourneyDetailsPage.enterDetailsExport(testPortOfEntry, None, testCountry)
      JourneyDetailsPage.clickContinue()
    }
  }
}
