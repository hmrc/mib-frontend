package model.exp

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import play.api.mvc.Session

class JourneyDetailsExpSpec extends WordSpec with Matchers with ScalaFutures {

  "session " should {
    "to-session " in {
      JourneyDetailsExp.toSession(JourneyDetailsExp("portOfExitExp", Option("eoriExp"), "destinationCountryExp")).toMap should contain allOf
        ("portOfExitExp" -> "portOfExitExp", "eoriExp" -> "eoriExp", "destinationCountryExp" -> "destinationCountryExp")

    }

    "from-session " in {
      JourneyDetailsExp.fromSession(Session(Map("portOfExitExp" -> "portOfExitExp", "eoriExp" -> "eoriExp", "destinationCountryExp" -> "destinationCountryExp"))) shouldBe Some(JourneyDetailsExp("portOfExitExp", Some("eoriExp"), "destinationCountryExp"))
    }

  }
  "getkeys " in {

    JourneyDetailsExp.getKeys shouldBe Seq("eoriExp", "portOfExitExp", "destinationCountryExp")

  }

}
