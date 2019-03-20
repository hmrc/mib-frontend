package model

import model.payapi.SpjRequest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json._

class SpjRequestSpec extends WordSpec with Matchers with ScalaFutures {

  "SpjRequest " should {
    "create valid json" in {

      val j: SpjRequest = SpjRequest(mibReference       = "MIBI1234567890^vat:500^duty:500", amountInPence = 1000,
                                     traderDetails      = "Microsoft<br>2390<br>Old Street Ber<br>Germany<br>EC1V 9EY",
                                     merchandiseDetails = "Parts and technical crew for the forest moon.")

      val expectedJson = Json.parse(
        s"""{
  "mibReference": "MIBI1234567890^vat:500^duty:500",
  "amountInPence" : 1000,
   "traderDetails" : "Microsoft<br>2390<br>Old Street Ber<br>Germany<br>EC1V 9EY",
   "merchandiseDetails": "Parts and technical crew for the forest moon."
}""")

      Json.toJson(j) shouldBe expectedJson
    }

  }
}
