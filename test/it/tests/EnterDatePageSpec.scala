package it.tests

import java.time.LocalDate

import it.pages.{EnterDatePage, EnterPurchasePricePage, PricesAndTaxesImportPage, SelectDeclarationPage}
import it.testsupport.BrowserSpec
import org.openqa.selenium.WebDriver
import it.testdata.TestData.purchasePriceExample
import it.pages.JourneyDetailsPage
class EnterDatePageSpec extends BrowserSpec {

  "show an error summary if nothing is entered" in new SetUp {
    goToPageVia()
    EnterDatePage.clickContinue()
    EnterDatePage.assertErrorSummaryIsShown()
  }
  "show an error summary if a past date is entered" in new SetUp {
    goToPageVia()
    EnterDatePage.enterDate(LocalDate.now().minusMonths(1))
    EnterDatePage.clickContinue()
    EnterDatePage.assertErrorSummaryIsShown()
  }
  "Go the next page for import in" in new SetUp {
    goToPageVia()
    EnterDatePage.enterDate()
    EnterDatePage.clickContinue()
    PricesAndTaxesImportPage.assertPageIsDisplayed()
  }

  "Go the next page for export in" in new SetUp {
    goToPageVia(asImport = false)
    EnterDatePage.enterDate()
    EnterDatePage.clickContinue()
    JourneyDetailsPage.assertExportPageIsDisplayed()
  }

  trait SetUp {
    def goToPageVia(asImport: Boolean = true)(implicit webDriver: WebDriver): Unit = {
      goTo(SelectDeclarationPage.path)
      if (asImport) SelectDeclarationPage.clickImport() else SelectDeclarationPage.clickExport()
      SelectDeclarationPage.clickContinue()
      EnterPurchasePricePage.enterIntoPurchasePrice(purchasePriceExample)
      EnterPurchasePricePage.clickContinue()
    }
  }
}
