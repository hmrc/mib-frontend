package it.pages

import org.openqa.selenium.{By, WebDriver}
import org.scalatest.Assertion

object SelectDeclarationPage extends CommonPage {

  val path = "/mib-frontend/start"

  def assertPageIsDisplayed()(implicit webDriver: WebDriver): Assertion = {
    currentPath shouldBe path
    getPageHeader shouldBe "Declare merchandise import"
  }
  def clickImport()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("import")).click())
  def clickExport()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("export")).click())
}
