package it.tests

import it.pages._
import it.testdata.TestData.{testCustomsDuty, testImportVat, purchasePriceExample}
import it.testsupport.BrowserSpec
import org.openqa.selenium.WebDriver

class TaxDuePageSpec extends BrowserSpec {

  "get to page" in new SetUp {
    goToPageVia()
    TaxDuePage.assertPageIsDisplayed()
  }

  "get to the next page" in new SetUp {
    goToPageVia()
    TaxDuePage.clickContinue()
    JourneyDetailsPage.assertImportPageIsDisplayed()
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
      PricesAndTaxesImportPage.enterDetails(testCustomsDuty, testImportVat)
      PricesAndTaxesImportPage.clickContinue()
    }
  }
}

