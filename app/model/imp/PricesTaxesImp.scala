package model.imp

import play.api.mvc.Session

final case class PricesTaxesImp(customsDuty: Double, importVat: Double)

object PricesTaxesImp {

  object Key {
    val CustomsDuty = "customsDutyImp"
    val ImportVat = "importVatImp"
  }

  def getKeys(): Seq[String] = {
    Seq(Key.CustomsDuty,
        Key.ImportVat)
  }

  def fromSession(session: Session): Option[PricesTaxesImp] = {
      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

      def mandatoryDouble(name: String): Double = session.get(name).getOrElse("").toDouble

    if (optional(Key.CustomsDuty).isEmpty)
      None
    else
      Some(PricesTaxesImp(
        mandatoryDouble(Key.CustomsDuty),
        mandatoryDouble(Key.ImportVat)
      ))
  }

  def toSession(page: PricesTaxesImp): Seq[(String, String)] = {
    Map(
      Key.CustomsDuty -> page.customsDuty.toString,
      Key.ImportVat -> page.importVat.toString
    ).toSeq
  }

}
