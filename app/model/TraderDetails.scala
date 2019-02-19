package model

import Service.CountriesService
import play.api.mvc.Session

case class TraderDetails(page: String = "trader_details", trader: String, line1: String, line2: Option[String] = None, city: Option[String] = None, county: Option[String] = None,
                         postcode: String, country: String, vrn: Option[String], vehicleRegNo: Option[String]) {

  val lineReturn = "<br>"

  def getFormattedAddress(countryName: String) =

    trader + lineReturn + line1 + lineReturn + line2.fold("")(_ + lineReturn) + city.fold("")(_ + lineReturn) +
      county.fold("")(_ + lineReturn) + postcode + lineReturn + countryName

}

object TraderDetails {

  object Key {
    val Page = "page"
    val Trader = "trader"
    val Line1 = "line1"
    val Line2 = "line2"
    val City = "city"
    val Postcode = "postcode"
    val County = "county"
    val Country = "country"
    val Vrn = "vrn"
    val VehicleRegNo = "vehicleRegNo"
  }
  def getKeys(): Seq[String] = {
    Seq(Key.City,
        Key.Country,
        Key.County,
        Key.Line1,
        Key.Line2,
        Key.Page,
        Key.Postcode,
        Key.Trader,
        Key.VehicleRegNo,
        Key.Vrn)
  }

  def fromSession(session: Session): Option[TraderDetails] = {
      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

    if (optional(Key.Line1).isEmpty)
      None
    else
      Some(TraderDetails(
        mandatory(Key.Page),
        mandatory(Key.Trader),
        mandatory(Key.Line1),
        optional(Key.Line2),
        optional(Key.City),
        optional(Key.County),
        mandatory(Key.Postcode),
        mandatory(Key.Country),
        optional(Key.Vrn),
        optional(Key.VehicleRegNo)))
  }

  def toSession(page2: TraderDetails): Seq[(String, String)] = {
    Map(
      Key.Page -> page2.page,
      Key.Trader -> page2.trader,
      Key.Line1 -> page2.line1,
      Key.Line2 -> page2.line2.getOrElse(""),
      Key.City -> page2.city.getOrElse(""),
      Key.County -> page2.county.getOrElse(""),
      Key.Postcode -> page2.postcode,
      Key.Country -> page2.country,
      Key.Vrn -> page2.vrn.getOrElse(""),
      Key.VehicleRegNo -> page2.vehicleRegNo.getOrElse("")).toSeq
  }

}

