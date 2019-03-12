package it.pages

import org.openqa.selenium.WebDriver
import org.scalatest.Assertion

object MerchandiseDetailsPage extends CommonPage {

  val pathExport = "/mib-frontend/export-page"
  val pathImport = "/mib-frontend/import-page"

  def assertExportPageIsDisplayed()(implicit webDriver: WebDriver): Assertion = {
    currentPath shouldBe pathExport
    getPageHeader shouldBe "Merchandise details"
  }

  def assertImportPageIsDisplayed()(implicit webDriver: WebDriver): Assertion = {
    currentPath shouldBe pathImport
    getPageHeader shouldBe "Merchandise details"
  }

  def enterValue(invoiceNumber: Option[String], desciptionOfGoods: String)(implicit driver: WebDriver): Unit = {
    invoiceNumber.foreach(textField("invoiceNumber").value = _)
    textArea("desciptionOfGoods").value = desciptionOfGoods
  }

}
