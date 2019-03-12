package it.pages

import org.openqa.selenium.{By, WebDriver}
import org.scalatest.Assertion

object DeclarationPage extends CommonPage {

  val pathExport = "/mib-frontend/export-page"

  def assertPageIsDisplayed()(implicit webDriver: WebDriver): Assertion = {
    currentPath shouldBe pathExport
    probing(_.findElement(By.className("bold-medium")).getText) shouldBe "Declaration reference"
  }

}
