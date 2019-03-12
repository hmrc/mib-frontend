package it.tests

import it.pages._
import it.testdata.TestData._
import it.testsupport.BrowserSpec
import org.openqa.selenium.WebDriver

class PriceAndTaxesPageSpec extends BrowserSpec {

  "show the Merchandise Details  page " in new SetUp {
    goToPageVia()
    PricesAndTaxesImportPage.assertPageIsDisplayed()
  }
  "go to the next page " in new SetUp {
    goToPageVia()
    PricesAndTaxesImportPage.enterDetails(testCustomsDuty, testImportVat)
    PricesAndTaxesImportPage.clickContinue()
    TaxDuePage.assertPageIsDisplayed()
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
    }
  }
}

