package controllers

import model.exp._
import play.api.data.Forms.{optional, _}
import play.api.data._
import play.api.data.validation.Constraints._

object FormsExp {

  def journeyDetailsExp: Form[JourneyDetailsExp] = {
    Form(mapping(
      "portOfExit" -> text.transform[String](_.trim, identity).verifying("error.invalid.portOfExit", s => s.length > 0).verifying(maxLength(100)),
      "eori" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(20))),
      "destinationCountry" -> text.transform[String](_.trim, identity).verifying("error.invalid.destinationCountry", s => s.length > 0).verifying(maxLength(100))
    )(JourneyDetailsExp.apply)(JourneyDetailsExp.unapply))
  }

  def traderDetailsCheckExp: Form[TraderDetailsCheckExp] = {
    Form(mapping(
      "nameAddress" -> text,
      "vrn" -> optional(text),
      "vehicleRegNo" -> optional(text)
    )(TraderDetailsCheckExp.apply)(TraderDetailsCheckExp.unapply))
  }

  def declarationReceived: Form[DeclarationReceived] = {
    Form(mapping(
      "currentDate" -> text,
      "traderNameAndAddress" -> text,
      "description" -> text,
      "mibReference" -> text
    )(DeclarationReceived.apply)(DeclarationReceived.unapply))
  }

}
