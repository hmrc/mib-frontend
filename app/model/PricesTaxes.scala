package model

import play.api.mvc.Session

case class PricesTaxes(page: String = "prices_taxes", purchasePrice: Double, customsDuty: Double, importVat: Double)

object PricesTaxes {

  object Key {
    val Page = "page"
    val PurchasePrice = "purchasePrice"
    val CustomsDuty = "customsDuty"
    val ImportVat = "importVat"
  }

  def getKeys(): Seq[String] = {
    Seq(Key.CustomsDuty,
        Key.ImportVat,
        Key.PurchasePrice,
        Key.Page)
  }

  def fromSession(session: Session): Option[PricesTaxes] = {
      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

      def mandatoryDouble(name: String): Double = session.get(name).getOrElse("").toDouble

    if (optional(Key.PurchasePrice).isEmpty)
      None
    else
      Some(PricesTaxes(
        mandatory(Key.Page),
        mandatoryDouble(Key.PurchasePrice),
        mandatoryDouble(Key.CustomsDuty),
        mandatoryDouble(Key.ImportVat)
      ))
  }

  def toSession(page5: PricesTaxes): Seq[(String, String)] = {
    Map(
      Key.Page -> page5.page,
      Key.PurchasePrice -> page5.purchasePrice.toString,
      Key.CustomsDuty -> page5.customsDuty.toString,
      Key.ImportVat -> page5.importVat.toString
    ).toSeq
  }

}
