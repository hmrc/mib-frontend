package model

import play.api.mvc.Session

case class JourneyDetails(page: String = "journey_details", portOfEntry: String, countryOfOrigin: String, eori: Option[String])

object JourneyDetails {

  object Key {
    val Page = "page"
    val PortOfEntry = "portOfEntry"
    val CountryOfOrigin = "countryOfOrigin"
    val Eori = "eori"
  }

  def fromSession(session: Session): Option[JourneyDetails] = {

      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

    if (optional(Key.PortOfEntry).isEmpty)
      None
    else
      Some(JourneyDetails(
        mandatory(Key.Page),
        mandatory(Key.PortOfEntry),
        mandatory(Key.CountryOfOrigin),
        optional(Key.Eori)))
  }

  def toSession(page1: JourneyDetails): Seq[(String, String)] = {
    Map(
      Key.Page -> page1.page,
      Key.PortOfEntry -> page1.portOfEntry,
      Key.CountryOfOrigin -> page1.countryOfOrigin,
      Key.Eori -> page1.eori.getOrElse("")).toSeq
  }

  def getKeys() = {
    Seq(Key.Page, Key.CountryOfOrigin, Key.Eori, Key.PortOfEntry)
  }

}
