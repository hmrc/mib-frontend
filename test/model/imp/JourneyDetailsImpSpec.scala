package model.imp

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import play.api.mvc.Session

class JourneyDetailsImpSpec extends WordSpec with Matchers with ScalaFutures {

  "session " should {
    "to-session " in {
      JourneyDetailsImp.toSession(JourneyDetailsImp("portOfEntryImp", "countryOfOriginImp", Option("eoriImp"))).toMap should contain allOf
        ("portOfEntryImp" -> "portOfEntryImp", "countryOfOriginImp" -> "countryOfOriginImp", "eoriImp" -> "eoriImp")

    }

    "from-session " in {
      JourneyDetailsImp.fromSession(Session(Map("portOfEntryImp" -> "portOfEntryImp", "countryOfOriginImp" -> "countryOfOriginImp", "eoriImp" -> "eoriImp"))) shouldBe Some(JourneyDetailsImp("portOfEntryImp", "countryOfOriginImp", Some("eoriImp")))
    }

  }
  "getkeys " in {

    JourneyDetailsImp.getKeys shouldBe Seq("countryOfOriginImp", "eoriImp", "portOfEntryImp")

  }

}
