package it.pages

import org.openqa.selenium.WebDriver
import org.scalatest.Assertion

object JourneyDetailsPage extends CommonPage {

  val pathExport = "/mib-frontend/export-page"
  val pathImport = "/mib-frontend/import-page"

  def assertExportPageIsDisplayed()(implicit webDriver: WebDriver): Assertion = {
    currentPath shouldBe pathExport
    getPageHeader shouldBe "Journey details"
  }

  def assertImportPageIsDisplayed()(implicit webDriver: WebDriver): Assertion = {
    currentPath shouldBe pathImport
    getPageHeader shouldBe "Journey details"
  }

  def enterDetailsExport(portOfEntry: String, eori: Option[String] = None)(implicit driver: WebDriver): Unit = {
    textField("portOfEntry").value = portOfEntry
    eori.foreach(a => numberField("eori").value = a)
  }

  def enterDetailsImport(portOfEntry: String, eori: String, country: String)(implicit driver: WebDriver): Unit = {
    textField("portOfEntry").value = portOfEntry
    numberField("eori").value = eori
    singleSel("countryOfOrigin").value = country
  }

}
