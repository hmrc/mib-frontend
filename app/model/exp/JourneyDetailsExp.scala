package model.exp

import play.api.mvc.Session

final case class JourneyDetailsExp(portOfExit: String, eori: Option[String], destinationCountry: String)

object JourneyDetailsExp {

  object Key {
    val PortOfExit = "portOfExitExp"
    val Eori = "eoriExp"
    val DestinationCountry = "destinationCountryExp"
  }

  def fromSession(session: Session): Option[JourneyDetailsExp] = {

      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

    if (optional(Key.PortOfExit).isEmpty)
      None
    else
      Some(JourneyDetailsExp(
        mandatory(Key.PortOfExit),
        optional(Key.Eori),
        mandatory(Key.DestinationCountry)))
  }

  def toSession(page: JourneyDetailsExp): Seq[(String, String)] = {
    Map(
      Key.PortOfExit -> page.portOfExit,
      Key.Eori -> page.eori.getOrElse(""),
      Key.DestinationCountry -> page.destinationCountry).toSeq
  }

  def getKeys() = {
    Seq(Key.Eori, Key.PortOfExit, Key.DestinationCountry)
  }

}
