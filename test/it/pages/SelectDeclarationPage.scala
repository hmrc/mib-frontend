package it.pages

import org.openqa.selenium.{By, WebDriver}
import org.scalatest.Assertion

object SelectDeclarationPage extends CommonPage {

  val path = "/mib-frontend/start"

  def assertPageIsDisplayed()(implicit webDriver: WebDriver): Assertion = {
    currentPath shouldBe path
    getPageHeader shouldBe "Select the type of declaration"
  }
  def clickImport()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("import")).click())
  def clickExport()(implicit driver: WebDriver): Unit = probing(_.findElement(By.id("export")).click())
}
