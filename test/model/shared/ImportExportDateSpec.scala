package model.shared

import org.scalatest.{Matchers, WordSpec}

class ImportExportDateSpec extends WordSpec with Matchers {

  "invalid leapyear" in {
    val date = ImportExportDate(29, 2, 2017)
    date.isValidDate shouldBe false
  }
  "valid leapyear" in {
    val date = ImportExportDate(29, 2, 2020)
    date.isValidDate shouldBe true
  }
  "valid year " in {
    val date = ImportExportDate(1, 1, 2019)
    date.isValidDate shouldBe true
  }
  "valid date1 " in {
    val date = ImportExportDate(9, 3, 2019)
    date.isValidDate shouldBe true
  }
  "invalid date1 " in {
    val date = ImportExportDate(31, 2, 2020)
    date.isValidDate shouldBe false
  }

}
