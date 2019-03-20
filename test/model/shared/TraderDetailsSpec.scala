package model.shared

import model.{MibTypes, YesNoValues}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import play.api.mvc.Session

class TraderDetailsSpec extends WordSpec with Matchers with ScalaFutures {

  "session " should {
    "to-session imp" in {

      TraderDetails.toSession(TraderDetails(trader            = "trader", line1 = Some("line1"), line2 = Some("line2"),
                                            city              = Some("city"), county = Some("county"), postcode = Some("postcode"), country = Some("country"),
                                            vrn               = Some("vrn"), vehicleRegNo = Some("vehicleRegNo"), line3 = Some("line3"),
                                            buildingAndStreet = Some("buildingAndStreet"), line2nonuk = Some("line2nonuk")), MibTypes.mibImport)
        .toMap should contain
      ("uk_Imp" -> "Yes", "trader_Imp" -> "trader", "line1_Imp" -> "line1", "line2_Imp" -> "line2", "city_Imp" -> "city", "county_Imp" -> "county", "postcode_Imp" -> "postcode", "country_Imp" -> "country", "vrn_Imp" -> "vrn", "vehicleRegNo_Imp" -> "vehicleRegNo", "line3_Imp" -> "line3", "buildingAndStreet_Imp" -> "buildingAndStreet", "line2nonuk_Imp" -> "line2nonuk")

    }

    "to-session exp" in {
      TraderDetails.toSession(TraderDetails(trader            = "trader", line1 = Some("line1"), line2 = Some("line2"),
                                            city              = Some("city"), county = Some("county"), postcode = Some("postcode"), country = Some("country"),
                                            vrn               = Some("vrn"), vehicleRegNo = Some("vehicleRegNo"), line3 = Some("line3"),
                                            buildingAndStreet = Some("buildingAndStreet"), line2nonuk = Some("line2nonuk")), MibTypes.mibExport)
        .toMap should contain
      ("uk_Exp" -> "Yes", "trader_Exp" -> "trader", "line1_Exp" -> "line1", "line2_Exp" -> "line2", "city_Exp" -> "city", "county_Exp" -> "county", "postcode_Exp" -> "postcode", "country_Exp" -> "country", "vrn_Exp" -> "vrn", "vehicleRegNo_Exp" -> "vehicleRegNo", "line3_Exp" -> "line3", "buildingAndStreet_Exp" -> "buildingAndStreet", "line2nonuk_Exp" -> "line2nonuk")
    }

    "from-session imp" in {
      TraderDetails.fromSession(Session(Map("uk_Imp" -> "Yes", "trader_Imp" -> "trader", "line1_Imp" -> "line1",
        "line2_Imp" -> "line2", "city_Imp" -> "city", "county_Imp" -> "county", "postcode_Imp" -> "postcode",
        "country_Imp" -> "country", "vrn_Imp" -> "vrn", "vehicleRegNo_Imp" -> "vehicleRegNo", "line3_Imp" -> "line3",
        "buildingAndStreet_Imp" -> "buildingAndStreet", "line2nonuk_Imp" -> "line2nonuk"
      )), MibTypes.mibImport) shouldBe
        Some(TraderDetails(trader            = "trader", line1 = Some("line1"), line2 = Some("line2"),
                           city              = Some("city"), county = Some("county"), postcode = Some("postcode"), country = Some("country"),
                           vrn               = Some("vrn"), vehicleRegNo = Some("vehicleRegNo"), line3 = Some("line3"),
                           buildingAndStreet = Some("buildingAndStreet"), line2nonuk = Some("line2nonuk")))
    }

    "from-session Exp" in {
      TraderDetails.fromSession(Session(Map("uk_Exp" -> "Yes", "trader_Exp" -> "trader", "line1_Exp" -> "line1",
        "line2_Exp" -> "line2", "city_Exp" -> "city", "county_Exp" -> "county", "postcode_Exp" -> "postcode",
        "country_Exp" -> "country", "vrn_Exp" -> "vrn", "vehicleRegNo_Exp" -> "vehicleRegNo", "line3_Exp" -> "line3",
        "buildingAndStreet_Exp" -> "buildingAndStreet", "line2nonuk_Exp" -> "line2nonuk"
      )), MibTypes.mibExport) shouldBe
        Some(TraderDetails(trader            = "trader", line1 = Some("line1"), line2 = Some("line2"),
                           city              = Some("city"), county = Some("county"), postcode = Some("postcode"), country = Some("country"),
                           vrn               = Some("vrn"), vehicleRegNo = Some("vehicleRegNo"), line3 = Some("line3"),
                           buildingAndStreet = Some("buildingAndStreet"), line2nonuk = Some("line2nonuk")))
    }

    "getkeys " should {
      "exp" in {
        TraderDetails.getKeys(MibTypes.mibExport) shouldBe Seq("city_Exp", "country_Exp", "county_Exp", "line1_Exp",
          "line2_Exp", "line3_Exp", "postcode_Exp", "trader_Exp", "vehicleRegNo_Exp", "vrn_Exp", "uk_Exp",
          "buildingAndStreet_Exp", "line2nonuk_Exp"
        )
      }
    }

    "getkeys " should {
      "imp" in {
        TraderDetails.getKeys(MibTypes.mibImport) shouldBe Seq("city_Imp", "country_Imp", "county_Imp", "line1_Imp",
          "line2_Imp", "line3_Imp", "postcode_Imp", "trader_Imp", "vehicleRegNo_Imp", "vrn_Imp", "uk_Imp",
          "buildingAndStreet_Imp", "line2nonuk_Imp"
        )
      }
    }

    "getAddress" should {

      val trader: TraderDetails = TraderDetails(trader            = "trader", line1 = Some("line1"), line2 = Some("line2"),
                                                city              = Some("city"), county = Some("county"), postcode = Some("postcode"), country = Some("country"),
                                                vrn               = Some("vrn"), vehicleRegNo = Some("vehicleRegNo"), line3 = Some("line3"),
                                                buildingAndStreet = Some("buildingAndStreet"), line2nonuk = Some("line2nonuk"))

      "UK" in {
        trader.getFormattedAddress("") shouldBe "trader<br>buildingAndStreet<br>line2<br>city<br>county<br>postcode"
      }

      "Non-UK" in {
        val tradernonuk: TraderDetails = trader.copy(uk = YesNoValues.no)
        tradernonuk.getFormattedAddress("Angola") shouldBe "trader<br>line1<br>line2nonuk<br>line3<br>Angola"
      }
    }

  }

}
