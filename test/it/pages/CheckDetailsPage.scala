package it.pages

import org.openqa.selenium.WebDriver
import org.scalatest.Assertion

object CheckDetailsPage extends CommonPage {

  val pathExport = "/mib-frontend/export-page"
  val importExport = "/mib-frontend/import-page"
  def assertExportPageIsDisplayed()(implicit webDriver: WebDriver): Assertion = {
    currentPath shouldBe pathExport
    getPageHeader shouldBe "Check export details"
  }
  def assertImportPageIsDisplayed()(implicit webDriver: WebDriver): Assertion = {
    currentPath shouldBe importExport
    getPageHeader shouldBe "Check import details"
  }

}
