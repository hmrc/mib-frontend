package model.shared

import model.MibTypes
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import play.api.mvc.Session

class ImportExportDateSpec extends WordSpec with Matchers with ScalaFutures {

  val validDates = Seq((1, 1, 2020),
    (10, 1, 2020), (31, 1, 2020),
    (1, 2, 2020), (10, 2, 2020), (29, 2, 2020),
    (1, 3, 2020), (10, 3, 2020), (31, 3, 2020),
    (1, 4, 2020), (10, 4, 2020), (30, 4, 2020),
    (1, 5, 2020), (10, 5, 2020), (31, 5, 2020),
    (1, 6, 2020), (10, 6, 2020), (30, 6, 2020),
    (1, 7, 2020), (10, 7, 2020), (31, 7, 2020),
    (1, 8, 2020), (10, 8, 2020), (31, 8, 2020),
    (1, 9, 2020), (10, 9, 2020), (30, 9, 2020),
    (1, 10, 2020), (10, 10, 2020), (31, 10, 2020),
    (1, 11, 2020), (10, 11, 2020), (30, 11, 2020),
    (1, 12, 2020), (10, 12, 2020), (31, 12, 2020)
  )

  val inValidDates = Seq((32, 1, 2020), (29, 2, 2019), (32, 3, 2020),
    (31, 4, 2020), (32, 5, 2020), (31, 6, 2020), (32, 7, 2020), (32, 8, 2020), (31, 9, 2020),
    (32, 10, 2020), (31, 11, 2020), (32, 12, 2020))

  "valid dates " should {

    validDates.foreach(dt => {
      "valid date " + dt._1 + "-" + dt._2 + "-" + dt._3 in {
        val date = ImportExportDate(dt._1, dt._2, dt._3)
        date.isValidDate shouldBe true
      }
    }

    )

  }

  "invalid dates " should {

    inValidDates.foreach(dt => {
      "invalid date " + dt._1 + "-" + dt._2 + "-" + dt._3 in {
        val date = ImportExportDate(dt._1, dt._2, dt._3)
        date.isValidDate shouldBe false
      }
    }

    )

  }
  "session " should {
    "to-session imp" in {
      ImportExportDate.toSession(ImportExportDate(10, 10, 2010), MibTypes.mibImport).toMap should contain allOf
        ("importExportDay_Imp" -> "10", "importExportMonth_Imp" -> "10", "importExportYear_Imp" -> "2010")

    }

    "from-session imp" in {
      ImportExportDate.fromSession(Session(Map("importExportDay_Imp" -> "10", "importExportMonth_Imp" -> "10", "importExportYear_Imp" -> "2010")), MibTypes.mibImport) shouldBe
        Some(ImportExportDate(10, 10, 2010))
    }

    "to-session exp" in {
      ImportExportDate.toSession(ImportExportDate(10, 10, 2010), MibTypes.mibExport).toMap should contain allOf
        ("importExportDay_Exp" -> "10", "importExportMonth_Exp" -> "10", "importExportYear_Exp" -> "2010")

    }

    "from-session exp" in {
      ImportExportDate.fromSession(Session(Map("importExportDay_Exp" -> "10", "importExportMonth_Exp" -> "10", "importExportYear_Exp" -> "2010")), MibTypes.mibExport) shouldBe
        Some(ImportExportDate(10, 10, 2010))
    }
  }

  "getkeys " should {
    "imp" in {
      ImportExportDate.getKeys(MibTypes.mibImport) shouldBe Seq("importExportDay_Imp", "importExportMonth_Imp", "importExportYear_Imp")
    }

    "exp" in {
      ImportExportDate.getKeys(MibTypes.mibExport) shouldBe Seq("importExportDay_Exp", "importExportMonth_Exp", "importExportYear_Exp")
    }
  }

}
