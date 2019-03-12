package it.pages

import org.openqa.selenium.WebDriver
import org.scalatest.Assertion

object TaxDuePage extends CommonPage {

  val path = "/mib-frontend/import-page"

  def assertPageIsDisplayed()(implicit webDriver: WebDriver): Assertion = {
    currentPath shouldBe path
    getPageHeader should startWith regex "Tax due "
  }
}
