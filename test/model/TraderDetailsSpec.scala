package model

import uk.gov.hmrc.play.test.UnitSpec

class TraderDetailsSpec extends UnitSpec {

  "check address format " in {
    val traderDetails: TraderDetails = new TraderDetails("page", "trader", "line1", Some("line2"), Some("city"), Some("county"),
      "BN12 4XL", "UK", Some("Vrn"), Some("vehiclereg"))

   // traderDetails.getFormattedAddress shouldBe "asfsf"
  }

}
