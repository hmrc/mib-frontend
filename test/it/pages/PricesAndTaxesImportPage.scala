package it.pages

import org.openqa.selenium.WebDriver
import org.scalatest.Assertion

object PricesAndTaxesImportPage extends CommonPage {

  val path = "/mib-frontend/import-page"

  def assertPageIsDisplayed()(implicit webDriver: WebDriver): Assertion = {
    currentPath shouldBe path
    getPageHeader shouldBe "Prices and taxes"
  }

  def enterDetails(customsDuty: String, importVat: String)(implicit driver: WebDriver): Unit = {
    numberField("customsDuty").value = customsDuty
    numberField("importVat").value = importVat
  }

}
