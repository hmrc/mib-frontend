package model.exp

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import play.api.mvc.Session

class JourneyDetailsExpSpec extends WordSpec with Matchers with ScalaFutures {

  "session " should {
    "to-session " in {
      JourneyDetailsExp.toSession(JourneyDetailsExp("portOfEntryExp", Option("eoriExp"))).toMap should contain allOf
        ("portOfEntryExp" -> "portOfEntryExp", "eoriExp" -> "eoriExp")

    }

    "from-session " in {
      JourneyDetailsExp.fromSession(Session(Map("portOfEntryExp" -> "portOfEntryExp", "eoriExp" -> "eoriExp"))) shouldBe Some(JourneyDetailsExp("portOfEntryExp", Some("eoriExp")))}

  }
  "getkeys " in {

    JourneyDetailsExp.getKeys shouldBe Seq("eoriExp", "portOfEntryExp")

  }

}
