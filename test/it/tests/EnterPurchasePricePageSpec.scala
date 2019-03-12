package it.tests

import it.pages.EnterPurchasePricePage.assertPageIsDisplayed
import it.pages.{EnterPurchasePricePage, SelectDeclarationPage}
import it.testsupport.BrowserSpec
import org.openqa.selenium.WebDriver
import it.testdata.TestData.purchasePriceExample
import it.pages.EnterDatePage
class EnterPurchasePricePageSpec extends BrowserSpec {

  "go to the correct page" in new SetUp {
    goToPageVia()
    assertPageIsDisplayed()
  }

  "show an error if nothing is entered into purchase des" in new SetUp {
    goToPageVia()
    EnterPurchasePricePage.clickContinue()
    EnterPurchasePricePage.assertErrorSummaryIsShown()
  }

  "go to the import enterDate page" in new SetUp {
    goToPageVia()
    EnterPurchasePricePage.enterIntoPurchasePrice(purchasePriceExample)
    EnterPurchasePricePage.clickContinue()
    EnterDatePage.assertImportPageIsDisplayed()
  }

  "go to the export enterDate page" in new SetUp {
    goToPageVia(asImport = false)
    EnterPurchasePricePage.enterIntoPurchasePrice(purchasePriceExample)
    EnterPurchasePricePage.clickContinue()
    EnterDatePage.assertExportPageIsDisplayed()
  }
  trait SetUp {
    def goToPageVia(asImport: Boolean = true)(implicit webDriver: WebDriver): Unit = {
      goTo(SelectDeclarationPage.path)
      if (asImport) SelectDeclarationPage.clickImport() else SelectDeclarationPage.clickExport()
      SelectDeclarationPage.clickContinue()
    }
  }
}
