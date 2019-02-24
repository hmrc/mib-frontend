package model.imp

import play.api.mvc.Session

case class MerchandiseDetailsImp(invoiceNumber: Option[String], desciptionOfGoods: String)

object MerchandiseDetailsImp {

  object Key {
    val InvoiceNumber = "invoiceNumberImp"
    val DesciptionOfGoods = "desciptionOfGoodsImp"
  }

  def getKeys(): Seq[String] = {
    Seq(Key.DesciptionOfGoods,
        Key.InvoiceNumber)
  }

  def fromSession(session: Session): Option[MerchandiseDetailsImp] = {
      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

    if (optional(Key.DesciptionOfGoods).isEmpty)
      None
    else
      Some(MerchandiseDetailsImp(
        optional(Key.InvoiceNumber),
        mandatory(Key.DesciptionOfGoods)))
  }

  def toSession(page3: MerchandiseDetailsImp): Seq[(String, String)] = {
    Map(
      Key.InvoiceNumber -> page3.invoiceNumber.getOrElse(""),
      Key.DesciptionOfGoods -> page3.desciptionOfGoods).toSeq
  }

}

