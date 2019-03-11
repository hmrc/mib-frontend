package model

import model.payapi.SpjResponse
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json._

class SpjResponseSpec extends WordSpec with Matchers with ScalaFutures {

  "SpjResponse " should {
    "create valid object" in {

      val response = Json.parse(
        s"""{
  "nextUrl" : "http://www.gov.uk",
   "journeyId" : "5c813ece700000b50004ffa2"
}""").as[SpjResponse]

      response.journeyId shouldBe "5c813ece700000b50004ffa2"
      response.nextUrl shouldBe "http://www.gov.uk"

    }

  }
}
