package model

import model.payapi.SpjRequest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json._

class JourneyRequestSpec extends WordSpec with Matchers with ScalaFutures {

  "JourneyRequest " should {
    "create valid json" in {

      val j: SpjRequest = SpjRequest(mibRef                 = "MIBI1234567890", amountInPence = 9700000,
                                     traderAddress          = "Microsoft<br>2390<br>Old Street Ber<br>Germany<br>EC1V 9EY",
                                     descriptionMerchandise = "Parts and technical crew for the forest moon.")

      val expectedJson = Json.parse(
        s"""{
  "mibRef": "MIBI1234567890",
  "amountInPence" : 9700000,
  "returnUrl" : "http://www.gov.uk",
   "backUrl" : "http://www.gov.uk",
   "traderAddress" : "Microsoft<br>2390<br>Old Street Ber<br>Germany<br>EC1V 9EY",
   "descriptionMerchandise": "Parts and technical crew for the forest moon."
}""")

      Json.toJson(j) shouldBe expectedJson
    }

  }
}
