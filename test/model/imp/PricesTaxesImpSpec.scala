package model.imp

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import play.api.mvc.Session

class PricesTaxesImpSpec extends WordSpec with Matchers with ScalaFutures {

  "session " should {
    "to-session " in {
      PricesTaxesImp.toSession(PricesTaxesImp(1.01, 1.02)).toMap should contain allOf
        ("customsDutyImp" -> "1.01", "importVatImp" -> "1.02")

    }

    "from-session " in {
      PricesTaxesImp.fromSession(Session(Map("customsDutyImp" -> "1.01", "importVatImp" -> "1.02"))) shouldBe Some(PricesTaxesImp(1.01,1.02))}

  }
  "getkeys " in {

    PricesTaxesImp.getKeys shouldBe Seq("customsDutyImp", "importVatImp")

  }

}
