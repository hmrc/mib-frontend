package it.tests

import it.pages.{EnterPurchasePricePage, SelectDeclarationPage}
import it.testsupport.BrowserSpec
import it.pages.SelectDeclarationPage.path
class SelectDeclarationPageSpec extends BrowserSpec {

  "go to the correct page" in {
    goTo(path)
    SelectDeclarationPage.assertPageIsDisplayed()
  }

  "show an error summary if nothing is selected and a user presses continue  " in {
    goTo(path)
    SelectDeclarationPage.clickContinue()
    SelectDeclarationPage.assertErrorSummaryIsShown()
  }

  "go to the enter purchase price page is the user clicks import " in {
    goTo(path)
    SelectDeclarationPage.clickImport()
    SelectDeclarationPage.clickContinue()
    EnterPurchasePricePage.assertPageIsDisplayed()
  }

  "go to the enter purchase price page is the user clicks export " in {
    goTo(path)
    SelectDeclarationPage.clickExport()
    SelectDeclarationPage.clickContinue()
    EnterPurchasePricePage.assertPageIsDisplayed()
  }

}
