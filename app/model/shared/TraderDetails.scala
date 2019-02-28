package model.shared

import model.MibType
import play.api.mvc.Session

case class TraderDetails(uk: String = "Yes", trader: String, line1: Option[String], line2: Option[String] = None,
                         city: Option[String] = None, county: Option[String] = None,
                         postcode: Option[String] = None, country: Option[String] = None, vrn: Option[String] = None,
                         vehicleRegNo: Option[String] = None, line3: Option[String] = None, buildingAndStreet: Option[String], line2nonuk: Option[String]) {

  val lineReturn = "<br>"

  def getFormattedAddress(countryName: String) = {

    uk match {
      case "Yes" => trader + lineReturn + buildingAndStreet.fold("")(_ + lineReturn) + line2.fold("")(_ + lineReturn) + city.fold("")(_ + lineReturn) +
        county.fold("")(_ + lineReturn) + postcode.fold("")(_ + lineReturn)
      case "No" => trader + lineReturn + line1.fold("")(_ + lineReturn) + line2nonuk.fold("")(_ + lineReturn) + line3.fold("")(_ + lineReturn) + countryName
    }

  }
}

object TraderDetails extends Shared {

  object Key {
    val Uk = "uk"
    val Trader = "trader"
    val Line1 = "line1"
    val Line2 = "line2"
    val Line3 = "line3"
    val City = "city"
    val Postcode = "postcode"
    val County = "county"
    val Country = "country"
    val Vrn = "vrn"
    val VehicleRegNo = "vehicleRegNo"
    val BuildingAndStreet = "buildingAndStreet"
    val Line2nonuk = "line2nonuk"
  }
  def getKeys(mibType: MibType): Seq[String] = {
    Seq(Key.City + mibType.caseValue,
      Key.Country + mibType.caseValue,
      Key.County + mibType.caseValue,
      Key.Line1 + mibType.caseValue,
      Key.Line2 + mibType.caseValue,
      Key.Line3 + mibType.caseValue,
      Key.Postcode + mibType.caseValue,
      Key.Trader + mibType.caseValue,
      Key.VehicleRegNo + mibType.caseValue,
      Key.Vrn + mibType.caseValue,
      Key.Uk + mibType.caseValue,
      Key.BuildingAndStreet + mibType.caseValue,
      Key.Line2nonuk + mibType.caseValue)
  }

  def fromSession(session: Session, mibType: MibType): Option[TraderDetails] = {

      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

    if (optional(Key.Uk + mibType.caseValue).isEmpty)
      None
    else
      Some(TraderDetails(
        mandatory(Key.Uk + mibType.caseValue),
        mandatory(Key.Trader + mibType.caseValue),
        optional(Key.Line1 + mibType.caseValue),
        optional(Key.Line2 + mibType.caseValue),
        optional(Key.City + mibType.caseValue),
        optional(Key.County + mibType.caseValue),
        optional(Key.Postcode + mibType.caseValue),
        optional(Key.Country + mibType.caseValue),
        optional(Key.Vrn + mibType.caseValue),
        optional(Key.VehicleRegNo + mibType.caseValue),
        optional(Key.Line3 + mibType.caseValue),
        optional(Key.BuildingAndStreet + mibType.caseValue),
        optional(Key.Line2nonuk + mibType.caseValue)))
  }

  def toSession(page: TraderDetails, mibType: MibType): Seq[(String, String)] = {
    Map(
      Key.Uk + mibType.caseValue -> page.uk,
      Key.Trader + mibType.caseValue -> page.trader,
      Key.Line1 + mibType.caseValue -> page.line1.getOrElse(""),
      Key.Line2 + mibType.caseValue -> page.line2.getOrElse(""),
      Key.City + mibType.caseValue -> page.city.getOrElse(""),
      Key.County + mibType.caseValue -> page.county.getOrElse(""),
      Key.Postcode + mibType.caseValue -> page.postcode.getOrElse(""),
      Key.Country + mibType.caseValue -> page.country.getOrElse(""),
      Key.Vrn + mibType.caseValue -> page.vrn.getOrElse(""),
      Key.VehicleRegNo + mibType.caseValue -> page.vehicleRegNo.getOrElse(""),
      Key.Line3 + mibType.caseValue -> page.line3.getOrElse(""),
      Key.BuildingAndStreet + mibType.caseValue -> page.buildingAndStreet.getOrElse(""),
      Key.Line2nonuk + mibType.caseValue -> page.line2nonuk.getOrElse("")
    ).toSeq
  }

}

