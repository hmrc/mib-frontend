package model.shared

import model.MibTypes
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import play.api.mvc.Session

class MerchandiseDetailsPageSpec extends WordSpec with Matchers with ScalaFutures {

  "session " should {
    "to-session imp" in {
      MerchandiseDetails.toSession(MerchandiseDetails(Some("A"), "B"), MibTypes.mibImport).toMap should contain allOf
        ("invoiceNumber_Imp" -> "A", "desciptionOfGoods_Imp" -> "B")

    }

    "from-session imp" in {
      MerchandiseDetails.fromSession(Session(Map("invoiceNumber_Imp" -> "A", "desciptionOfGoods_Imp" -> "B")), MibTypes.mibImport) shouldBe
        Some(MerchandiseDetails(Some("A"), "B"))
    }

    "to-session exp" in {
      MerchandiseDetails.toSession(MerchandiseDetails(Some("A"), "B"), MibTypes.mibExport).toMap should contain allOf
        ("invoiceNumber_Exp" -> "A", "desciptionOfGoods_Exp" -> "B")

    }

    "from-session exp" in {
      MerchandiseDetails.fromSession(Session(Map("invoiceNumber_Exp" -> "A", "desciptionOfGoods_Exp" -> "B")), MibTypes.mibExport) shouldBe
        Some(MerchandiseDetails(Some("A"), "B"))
    }

  }

  "getkeys " should {
    "imp" in {
      MerchandiseDetails.getKeys(MibTypes.mibImport) shouldBe Seq("desciptionOfGoods_Imp", "invoiceNumber_Imp")
    }

    "exp" in {
      MerchandiseDetails.getKeys(MibTypes.mibExport) shouldBe Seq("desciptionOfGoods_Exp", "invoiceNumber_Exp")
    }
  }

}
