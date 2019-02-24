package model

import uk.gov.hmrc.play.test.UnitSpec


import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ Matchers, WordSpec }
import play.api.mvc.Session


class TraderDetailsSpec extends WordSpec with Matchers with ScalaFutures {
//
//  "toSession" should {
//    "convert billing Address into a Seq of Tuples with all fields" in {
//      Address.toSession(Address("Flat 144", Some("Lavington Street"), Some("London"), None, "SE1 9NN", "UK", Some("sau@hotmail.com"))).toMap should contain allOf
//        ("line1" -> "Flat 144", "line2" -> "Lavington Street", "city" -> "London", "county" -> "", "postcode" -> "SE1 9NN", "country" -> "UK", "emailaddress" -> "sau@hotmail.com")
//    }
//
//    "convert billing Address into a Seq of Tuples without optional field" in {
//      Address.toSession(Address("Flat 144", None, None, None, "SE1 9NN", "UK", None)).toMap should contain allOf
//        ("line1" -> "Flat 144", "line2" -> "", "city" -> "", "county" -> "", "postcode" -> "SE1 9NN", "country" -> "UK", "emailaddress" -> "")
//    }
//    "add in the email-provided key to true if a email is provided" in {
//      Address.toSession(Address("Flat 144", None, None, None, "SE1 9NN", "UK", Some("sau@hotmail.com"))).toMap should contain allOf
//        ("line1" -> "Flat 144", "line2" -> "", "city" -> "",
//          "county" -> "", "postcode" -> "SE1 9NN", "country" -> "UK", "emailaddress" -> "sau@hotmail.com",
//          OurSessionKeys.EmailProvided -> "true")
//    }
//
//    "add in the email-provided key to false if a email is not provided" in {
//      Address.toSession(Address("Flat 144", None, None, None, "SE1 9NN", "UK", None)).toMap should contain allOf
//        ("line1" -> "Flat 144", "line2" -> "", "city" -> "",
//          "county" -> "", "postcode" -> "SE1 9NN", "country" -> "UK", "emailaddress" -> "",
//          OurSessionKeys.EmailProvided -> "false")
//    }
//  }
//
//  "fromSession" should {
//    "a Seq of Tuples into Address with all fields" in {
//      Address.fromSession(Session(Map("line1" -> "Flat 144", "line2" -> "Lavington Street", "city" -> "London", "county" -> "", "postcode" -> "SE1 9NN", "country" -> "UK", "emailaddress" -> "sau@hotmail.com"))) shouldBe
//        Some(Address("Flat 144", Some("Lavington Street"), Some("London"), None, "SE1 9NN", "UK", Some("sau@hotmail.com")))
//    }
//
//    "a Seq of Tuples into Address without optional fields" in {
//      Address.fromSession(Session(Map("line1" -> "Flat 144", "line2" -> "", "city" -> "", "county" -> "", "postcode" -> "SE1 9NN", "country" -> "UK"))) shouldBe
//        Some(Address("Flat 144", None, None, None, "SE1 9NN", "UK", None))
//    }
//
//    "return None if line1 is missing" in {
//      Address.fromSession(Session(Map("line1" -> "", "line2" -> "", "city" -> "", "county" -> "", "postcode" -> "SE1 9NN", "country" -> "UK"))) shouldBe
//        None
//    }
//
//  }

}
