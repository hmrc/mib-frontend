package it.pages

import org.openqa.selenium.WebDriver
import org.scalatest.Assertion

object EnterPurchasePricePage extends CommonPage {

  val path = "/mib-frontend/submit-select-dec-type"

  def assertPageIsDisplayed()(implicit webDriver: WebDriver): Assertion = {
    currentPath shouldBe path
    getPageHeader shouldBe "What was the purchase prices ?"
  }

  def enterIntoPurchasePrice(value: String)(implicit driver: WebDriver): Unit = {
    textField("purchasePrice").value = value
  }
}
