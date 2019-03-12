package it.pages

import java.time.LocalDate

import org.openqa.selenium.WebDriver
import org.scalatest.Assertion

object EnterDatePage extends CommonPage {

  val pathImport = "/mib-frontend/import-page"
  val pathExport = "/mib-frontend/export-page"

  def assertImportPageIsDisplayed()(implicit webDriver: WebDriver): Assertion = {
    currentPath shouldBe pathImport
    getPageHeader shouldBe "Import date"
  }

  def assertExportPageIsDisplayed()(implicit webDriver: WebDriver): Assertion = {
    currentPath shouldBe pathExport
    getPageHeader shouldBe "Export date"
  }

  def enterDate(value: LocalDate = LocalDate.now())(implicit driver: WebDriver): Unit = {
    numberField("importExportDay").value = value.getDayOfMonth.toString
    numberField("importExportMonth").value = value.getMonthValue.toString
    numberField("importExportYear").value = value.getYear.toString
  }

}
