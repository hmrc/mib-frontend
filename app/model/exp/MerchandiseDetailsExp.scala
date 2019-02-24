package model.exp

import play.api.mvc.Session

case class MerchandiseDetailsExp(invoiceNumber: Option[String], desciptionOfGoods: String)

object MerchandiseDetailsExp {

  object Key {
    val InvoiceNumber = "invoiceNumberExp"
    val DesciptionOfGoods = "desciptionOfGoodsExp"
  }

  def getKeys(): Seq[String] = {
    Seq(Key.DesciptionOfGoods,
        Key.InvoiceNumber)
  }

  def fromSession(session: Session): Option[MerchandiseDetailsExp] = {
      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

    if (optional(Key.DesciptionOfGoods).isEmpty)
      None
    else
      Some(MerchandiseDetailsExp(
        optional(Key.InvoiceNumber),
        mandatory(Key.DesciptionOfGoods)))
  }

  def toSession(page3: MerchandiseDetailsExp): Seq[(String, String)] = {
    Map(
      Key.InvoiceNumber -> page3.invoiceNumber.getOrElse(""),
      Key.DesciptionOfGoods -> page3.desciptionOfGoods).toSeq
  }

}

