package it.pages

import model.shared.TraderDetails
import org.openqa.selenium.{By, WebDriver}
import org.scalatest.Assertion

object DetailsOfTraderPage extends CommonPage {

  val pathExport = "/mib-frontend/export-page"
  val pathImport = "/mib-frontend/import-page"
  def assertExportPageIsDisplayed()(implicit webDriver: WebDriver): Assertion = {
    currentPath shouldBe pathExport
    getPageHeader shouldBe "Details of the business moving goods"
  }
  def assertImportPageIsDisplayed()(implicit webDriver: WebDriver): Assertion = {
    currentPath shouldBe pathImport
    getPageHeader shouldBe "Details of the business moving goods"
  }
  def clickUk()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("ukaddress-label")).click())

  def clickNotUk()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("nonukaddress-label")).click())

  def enterDetails(trader: TraderDetails, isUK: Boolean = true)(implicit driver: WebDriver): Unit = {
    textField("trader").value = trader.trader
    if (isUK) {
      clickUk
      fillInUkAddress(trader)
    } else {
      clickNotUk
      fillInNonUkAddress(trader)
    }
    trader.vrn.foreach(textField("vrn").value = _)
    trader.vehicleRegNo.foreach(textField("vehicleRegNo").value = _)

  }

  private def fillInUkAddress(trader: TraderDetails)(implicit driver: WebDriver): Unit = {

    trader.buildingAndStreet.foreach(textField("buildingAndStreet").value = _)
    trader.line2.foreach(textField("line2").value = _)
    trader.city.foreach(textField("city").value = _)
    trader.county.foreach(textField("county").value = _)
    trader.postcode.foreach(textField("postcode").value = _)
  }

  private def fillInNonUkAddress(trader: TraderDetails)(implicit driver: WebDriver): Unit = {
    trader.line1.foreach(textField("line1").value = _)
    trader.line2nonuk.foreach(textField("line2nonuk").value = _)
    trader.line3.foreach(textField("line3").value = _)
    trader.country.foreach(singleSel("country").value = _)
  }
}
