package model.exp

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import play.api.mvc.Session

class DeclarationReceivedSpec extends WordSpec with Matchers with ScalaFutures {

  "session " should {
    "to-session " in {
      DeclarationReceived.toSession(DeclarationReceived("currentDate", "traderNameAndAddress", "description", "mibReference")).toMap should contain allOf
        ("currentDate" -> "currentDate", "traderNameAndAddress" -> "traderNameAndAddress", "description" -> "description", "mibReference" -> "mibReference")

    }

    "from-session " in {
      DeclarationReceived.fromSession(Session(Map("currentDate" -> "currentDate", "traderNameAndAddress" -> "traderNameAndAddress",
        "description" -> "description", "mibReference" -> "mibReference"))) shouldBe Some(DeclarationReceived("currentDate", "traderNameAndAddress", "description", "mibReference"))}

  }
  "getkeys " in {

      DeclarationReceived.getKeys shouldBe Seq("traderNameAndAddress", "currentDate", "description", "mibReference")

  }

}
