package model.shared

import model.MibType
import play.api.mvc.Session

final case class Prices(purchasePrice: Double)

object Prices extends Shared {

  object Key {
    val PurchasePrice = "purchasePrice"
  }

  def getKeys(mibType: MibType): Seq[String] = {
    Seq(Key.PurchasePrice + appendVal(mibType))
  }

  def fromSession(session: Session, mibType: MibType): Option[Prices] = {

      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

      def mandatoryDouble(name: String): Double = session.get(name).getOrElse("").toDouble

    if (optional(Key.PurchasePrice + appendVal(mibType)).isEmpty)
      None
    else
      Some(Prices(
        mandatoryDouble(Key.PurchasePrice + appendVal(mibType))
      ))
  }

  def toSession(page: Prices, mibType: MibType): Seq[(String, String)] = {

    Map(
      Key.PurchasePrice + appendVal(mibType) -> page.purchasePrice.toString
    ).toSeq
  }

}
