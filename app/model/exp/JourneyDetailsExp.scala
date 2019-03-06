package model.exp

import play.api.mvc.Session

case class JourneyDetailsExp(portOfEntry: String, eori: Option[String])

object JourneyDetailsExp {

  object Key {
    val PortOfEntry = "portOfEntryExp"
    val Eori = "eoriExp"
  }

  def fromSession(session: Session): Option[JourneyDetailsExp] = {

      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

    if (optional(Key.PortOfEntry).isEmpty)
      None
    else
      Some(JourneyDetailsExp(
        mandatory(Key.PortOfEntry),
        optional(Key.Eori)))
  }

  def toSession(page: JourneyDetailsExp): Seq[(String, String)] = {
    Map(
      Key.PortOfEntry -> page.portOfEntry,
      Key.Eori -> page.eori.getOrElse("")).toSeq
  }

  def getKeys() = {
    Seq(Key.Eori, Key.PortOfEntry)
  }

}
