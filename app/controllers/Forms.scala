package controllers

import model.{ImportDec, SelectDecType}
import play.api.data.Forms._
import play.api.data._

object Forms {

  val selectDecType: Form[SelectDecType] = {
    Form(mapping(
      "select_dec_type" -> optional(text).verifying("select.dec.type.error", _.nonEmpty))(SelectDecType.apply)(SelectDecType.unapply))
  }

  val importDec: Form[ImportDec] = {
    Form(mapping(
      "portOfEntry" -> nonEmptyText.transform[String](_.trim, identity),
      "countryOfOrigin" -> nonEmptyText.transform[String](_.trim, identity),
      "eori" -> optional(text.transform[String](_.trim, identity))
    )(ImportDec.apply)(ImportDec.unapply))
  }

}
