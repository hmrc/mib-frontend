package model.shared

import model.MibType
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.Session

final case class MerchandiseDetails(invoiceNumber: Option[String], desciptionOfGoods: String)

object MerchandiseDetails extends Shared {

  object Key {
    val InvoiceNumber = "invoiceNumber"
    val DesciptionOfGoods = "desciptionOfGoods"
  }

  def getKeys(mibType: MibType): Seq[String] = {
    Seq(Key.DesciptionOfGoods + appendVal(mibType),
      Key.InvoiceNumber + appendVal(mibType))
  }

  def fromSession(session: Session, mibType: MibType): Option[MerchandiseDetails] = {
      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

    if (optional(Key.DesciptionOfGoods + appendVal(mibType)).isEmpty)
      None
    else
      Some(MerchandiseDetails(
        optional(Key.InvoiceNumber + appendVal(mibType)),
        mandatory(Key.DesciptionOfGoods + appendVal(mibType))))
  }

  def toSession(page: MerchandiseDetails, mibType: MibType): Seq[(String, String)] = {
    Map(
      Key.InvoiceNumber + appendVal(mibType) -> page.invoiceNumber.getOrElse(""),
      Key.DesciptionOfGoods + appendVal(mibType) -> page.desciptionOfGoods).toSeq
  }

  implicit val merchandiseDetailsFormat: OFormat[MerchandiseDetails] = Json.format[MerchandiseDetails]

}

