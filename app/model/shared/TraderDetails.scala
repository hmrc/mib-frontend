package model.shared

import audit.{NonUkAddress, UkAddress}
import model.{MibType, YesNoValues}
import play.api.mvc.Session

final case class TraderDetails(uk: String = YesNoValues.yes, trader: String, line1: Option[String], line2: Option[String] = None,
                               city: Option[String] = None, county: Option[String] = None,
                               postcode: Option[String] = None, country: Option[String] = None, vrn: Option[String] = None,
                               vehicleRegNo: Option[String] = None, line3: Option[String] = None, buildingAndStreet: Option[String],
                               line2nonuk: Option[String]) {

  val lineReturn = "<br>"

  def getFormattedAddress(countryName: String): String = {

    uk match {
      case YesNoValues.yes => trader + lineReturn + buildingAndStreet.fold("")(_ + lineReturn) + line2.fold("")(_ + lineReturn) + city.fold("")(_ + lineReturn) +
        county.fold("")(_ + lineReturn) + postcode.fold("")(_ + "")
      case YesNoValues.no => trader + lineReturn + line1.fold("")(_ + lineReturn) + line2nonuk.fold("")(_ + lineReturn) + line3.fold("")(_ + lineReturn) + countryName
    }

  }

  def getAddressObjectNonUk(countryName: String): NonUkAddress = {
    NonUkAddress(line1.get, line2nonuk, line3, countryName)
  }

  def getAddressObjectUk(): UkAddress = {
    UkAddress(buildingAndStreet.get, line2, city, county, postcode.get)
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
    Seq(Key.City + appendVal(mibType),
      Key.Country + appendVal(mibType),
      Key.County + appendVal(mibType),
      Key.Line1 + appendVal(mibType),
      Key.Line2 + appendVal(mibType),
      Key.Line3 + appendVal(mibType),
      Key.Postcode + appendVal(mibType),
      Key.Trader + appendVal(mibType),
      Key.VehicleRegNo + appendVal(mibType),
      Key.Vrn + appendVal(mibType),
      Key.Uk + appendVal(mibType),
      Key.BuildingAndStreet + appendVal(mibType),
      Key.Line2nonuk + appendVal(mibType))
  }

  def fromSession(session: Session, mibType: MibType): Option[TraderDetails] = {

      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

    if (optional(Key.Uk + appendVal(mibType)).isEmpty)
      None
    else
      Some(TraderDetails(
        mandatory(Key.Uk + appendVal(mibType)),
        mandatory(Key.Trader + appendVal(mibType)),
        optional(Key.Line1 + appendVal(mibType)),
        optional(Key.Line2 + appendVal(mibType)),
        optional(Key.City + appendVal(mibType)),
        optional(Key.County + appendVal(mibType)),
        optional(Key.Postcode + appendVal(mibType)),
        optional(Key.Country + appendVal(mibType)),
        optional(Key.Vrn + appendVal(mibType)),
        optional(Key.VehicleRegNo + appendVal(mibType)),
        optional(Key.Line3 + appendVal(mibType)),
        optional(Key.BuildingAndStreet + appendVal(mibType)),
        optional(Key.Line2nonuk + appendVal(mibType))))
  }

  def toSession(page: TraderDetails, mibType: MibType): Seq[(String, String)] = {
    Map(
      Key.Uk + appendVal(mibType) -> page.uk,
      Key.Trader + appendVal(mibType) -> page.trader,
      Key.Line1 + appendVal(mibType) -> page.line1.getOrElse(""),
      Key.Line2 + appendVal(mibType) -> page.line2.getOrElse(""),
      Key.City + appendVal(mibType) -> page.city.getOrElse(""),
      Key.County + appendVal(mibType) -> page.county.getOrElse(""),
      Key.Postcode + appendVal(mibType) -> page.postcode.getOrElse(""),
      Key.Country + appendVal(mibType) -> page.country.getOrElse(""),
      Key.Vrn + appendVal(mibType) -> page.vrn.getOrElse(""),
      Key.VehicleRegNo + appendVal(mibType) -> page.vehicleRegNo.getOrElse(""),
      Key.Line3 + appendVal(mibType) -> page.line3.getOrElse(""),
      Key.BuildingAndStreet + appendVal(mibType) -> page.buildingAndStreet.getOrElse(""),
      Key.Line2nonuk + appendVal(mibType) -> page.line2nonuk.getOrElse("")
    ).toSeq
  }

}

