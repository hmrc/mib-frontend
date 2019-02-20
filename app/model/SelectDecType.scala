package model

import play.api.mvc.Session

final case class SelectDecType(chosenDecType: Option[String])

object SelectDecType {

  object Key {
    val ChosenDecType = "chosenDecType"
  }

  def getKeys(): Seq[String] = {
    Seq(Key.ChosenDecType)
  }

  def fromSession(session: Session): Option[SelectDecType] = {
      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

    if (optional(Key.ChosenDecType).isEmpty)
      None
    else
      Some(SelectDecType(
        optional(Key.ChosenDecType))
      )
  }

  def toSession(page: SelectDecType): Seq[(String, String)] = {
    Map(
      Key.ChosenDecType -> page.chosenDecType.getOrElse("")
    ).toSeq
  }

}

