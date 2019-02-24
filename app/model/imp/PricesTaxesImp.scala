package model.imp

import play.api.mvc.Session

case class PricesTaxesImp(purchasePrice: Double, customsDuty: Double, importVat: Double)

object PricesTaxesImp {

  object Key {
    val PurchasePrice = "purchasePriceImp"
    val CustomsDuty = "customsDutyImp"
    val ImportVat = "importVatImp"
  }

  def getKeys(): Seq[String] = {
    Seq(Key.CustomsDuty,
        Key.ImportVat,
        Key.PurchasePrice)
  }

  def fromSession(session: Session): Option[PricesTaxesImp] = {
      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

      def mandatoryDouble(name: String): Double = session.get(name).getOrElse("").toDouble

    if (optional(Key.PurchasePrice).isEmpty)
      None
    else
      Some(PricesTaxesImp(
        mandatoryDouble(Key.PurchasePrice),
        mandatoryDouble(Key.CustomsDuty),
        mandatoryDouble(Key.ImportVat)
      ))
  }

  def toSession(page5: PricesTaxesImp): Seq[(String, String)] = {
    Map(
      Key.PurchasePrice -> page5.purchasePrice.toString,
      Key.CustomsDuty -> page5.customsDuty.toString,
      Key.ImportVat -> page5.importVat.toString
    ).toSeq
  }

}
