package model

import play.api.mvc.Session

case class MerchandiseDetails(page: String = "merchandise_details", invoiceNumber: Option[String], desciptionOfGoods: String)

object MerchandiseDetails {

  object Key {
    val Page = "page"
    val InvoiceNumber = "invoiceNumber"
    val DesciptionOfGoods = "desciptionOfGoods"
  }

  def getKeys(): Seq[String] = {
    Seq(Key.DesciptionOfGoods,
        Key.InvoiceNumber,
        Key.Page)
  }

  def fromSession(session: Session): Option[MerchandiseDetails] = {
      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

    if (optional(Key.DesciptionOfGoods).isEmpty)
      None
    else
      Some(MerchandiseDetails(
        mandatory(Key.Page),
        optional(Key.InvoiceNumber),
        mandatory(Key.DesciptionOfGoods)))
  }

  def toSession(page3: MerchandiseDetails): Seq[(String, String)] = {
    Map(
      Key.Page -> page3.page,
      Key.InvoiceNumber -> page3.invoiceNumber.getOrElse(""),
      Key.DesciptionOfGoods -> page3.desciptionOfGoods).toSeq
  }

}

