package model.imp

import play.api.mvc.Session

case class JourneyDetailsImp(portOfEntry: String, countryOfOrigin: String, eori: Option[String])

object JourneyDetailsImp {

  object Key {
    val PortOfEntry = "portOfEntryImp"
    val CountryOfOrigin = "countryOfOriginImp"
    val Eori = "eoriImp"
  }

  def fromSession(session: Session): Option[JourneyDetailsImp] = {

      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

    if (optional(Key.PortOfEntry).isEmpty)
      None
    else
      Some(JourneyDetailsImp(
        mandatory(Key.PortOfEntry),
        mandatory(Key.CountryOfOrigin),
        optional(Key.Eori)))
  }

  def toSession(page1: JourneyDetailsImp): Seq[(String, String)] = {
    Map(
      Key.PortOfEntry -> page1.portOfEntry,
      Key.CountryOfOrigin -> page1.countryOfOrigin,
      Key.Eori -> page1.eori.getOrElse("")).toSeq
  }

  def getKeys() = {
    Seq(Key.CountryOfOrigin, Key.Eori, Key.PortOfEntry)
  }

}
