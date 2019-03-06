package model.exp

import play.api.mvc.Session

case class DeclarationReceived(currentDate: String, traderNameAndAddress: String, description: String, mibReference: String)

object DeclarationReceived {

  object Key {
    val CurrentDate = "currentDateExp"
    val TraderNameAndAddress = "traderNameAndAddressExp"
    val Description = "descriptionExp"
    val MibReference = "mibReferenceExp"
  }

  def fromSession(session: Session): Option[DeclarationReceived] = {

      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

    if (optional(Key.CurrentDate).isEmpty)
      None
    else
      Some(DeclarationReceived(
        mandatory(Key.CurrentDate),
        mandatory(Key.TraderNameAndAddress),
        mandatory(Key.Description),
        mandatory(Key.MibReference)))
  }

  def toSession(page: DeclarationReceived): Seq[(String, String)] = {
    Map(
      Key.CurrentDate -> page.currentDate,
      Key.TraderNameAndAddress -> page.traderNameAndAddress,
      Key.Description -> page.description,
      Key.MibReference -> page.mibReference).toSeq
  }

  def getKeys() = {
    Seq(Key.TraderNameAndAddress, Key.CurrentDate, Key.Description, Key.MibReference)
  }

}

