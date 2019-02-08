package controllers

import model.SelectDecType
import play.api.data.Forms._
import play.api.data._

object Forms {

  val selectDecType: Form[SelectDecType] = {
    Form(mapping(
      "select_dec_type" -> optional(text).verifying("select.dec.type.error", _.nonEmpty))(SelectDecType.apply)(SelectDecType.unapply))
  }

}

