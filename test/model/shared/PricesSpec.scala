package model.shared

import model.MibTypes
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import play.api.mvc.Session

class PricesSpec extends WordSpec with Matchers with ScalaFutures {

  "session " should {
    "to-session imp" in {
      Prices.toSession(Prices(100), MibTypes.mibImport).toMap should contain
      ("purchasePrice_Imp" -> "100")

    }

    "from-session imp" in {
      Prices.fromSession(Session(Map("purchasePrice_Imp" -> "100")), MibTypes.mibImport) shouldBe
        Some(Prices(100))
    }

    "to-session exp" in {
      Prices.toSession(Prices(100), MibTypes.mibExport).toMap should contain
      ("purchasePrice_Exp" -> "100")

    }

    "from-session exp" in {
      Prices.fromSession(Session(Map("purchasePrice_Exp" -> "100")), MibTypes.mibExport) shouldBe
        Some(Prices(100))
    }

  }

  "getkeys " should {
    "imp" in {
      Prices.getKeys(MibTypes.mibImport) shouldBe Seq("purchasePrice_Imp")
    }

    "exp" in {
      Prices.getKeys(MibTypes.mibExport) shouldBe Seq("purchasePrice_Exp")
    }
  }

}
