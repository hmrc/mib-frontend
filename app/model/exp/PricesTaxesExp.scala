package model.exp

import play.api.mvc.Session

case class PricesTaxesExp(purchasePrice: Double)

object PricesTaxesExp {

  object Key {
    val PurchasePrice = "purchasePriceExp"
  }

  def getKeys(): Seq[String] = {
    Seq(Key.PurchasePrice)
  }

  def fromSession(session: Session): Option[PricesTaxesExp] = {
      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

      def mandatoryDouble(name: String): Double = session.get(name).getOrElse("").toDouble

    if (optional(Key.PurchasePrice).isEmpty)
      None
    else
      Some(PricesTaxesExp(
        mandatoryDouble(Key.PurchasePrice)
      ))
  }

  def toSession(page5: PricesTaxesExp): Seq[(String, String)] = {
    Map(
      Key.PurchasePrice -> page5.purchasePrice.toString
    ).toSeq
  }

}
