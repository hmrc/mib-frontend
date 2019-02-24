package controllers

import model.exp.{DeclareExp, TraderDetailsCheckExp, _}
import model.imp.TraderDetailsImp
import play.api.data.Forms.{optional, _}
import play.api.data._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints._

object FormsExp {

  def journeyDetailsExp: Form[JourneyDetailsExp] = {
    Form(mapping(
      "portOfEntry" -> text.transform[String](_.trim, identity).verifying("error.invalid.portOfEntry", s => s.length > 0).verifying(maxLength(100)),
      "eori" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(20)))
    )(JourneyDetailsExp.apply)(JourneyDetailsExp.unapply))
  }

  def traderDetailsExp: Form[TraderDetailsExp] = {
    Form(mapping(
      "trader" -> text.transform[String](_.trim, identity).verifying(maxLength(100)),
      "line1" -> text.transform[String](_.trim, identity).verifying("error.invalid.addressline1", s => s.length > 0)
        .verifying(maxLength(100)),
      "line2" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(100))),
      "city" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(60))),
      "county" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(60))),
      "postcode" -> of(CommonForms.postcodeFormatter),
      "country" -> text.verifying(CommonForms.LocalConstraint.countryConstraint),
      "vrn" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(60))),
      "vehicleRegNo" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(60)))
    )(TraderDetailsExp.apply)(TraderDetailsExp.unapply))
  }
  def merchandiseDetailsExp: Form[MerchandiseDetailsExp] = {
    Form(mapping(
      "invoiceNumber" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(20))),
      "desciptionOfGoods" -> text.transform[String](_.trim, identity).verifying("error.invalid.desciptionOfGoods", s => s.length > 0).verifying(maxLength(1200))
    )(MerchandiseDetailsExp.apply)(MerchandiseDetailsExp.unapply))
  }

  //TODO need better validation
  def departureDecDatesExp: Form[DepartureDecDatesExp] = {
    Form(mapping(
      "departureDay" -> number(min = 1, max = 31),
      "departureMonth" -> number(min = 1, max = 12),
      "departureYear" -> number(min = 1900, max = 2100),
      "declarationDay" -> number(min = 1, max = 31),
      "declarationMonth" -> number(min = 1, max = 12),
      "declarationYear" -> number(min = 1900, max = 2100)
    )(DepartureDecDatesExp.apply)(DepartureDecDatesExp.unapply))
  }

  def pricesTaxesExp: Form[PricesTaxesExp] = {
    Form(mapping(
      "purchasePrice" -> of(doubleFormat)
    )(PricesTaxesExp.apply)(PricesTaxesExp.unapply))
  }

  def traderDetailsCheckExp: Form[TraderDetailsCheckExp] = {
    Form(mapping(
      "nameAddress" -> text,
      "vrn" -> optional(text),
      "vehicleRegNo" -> optional(text)
    )(TraderDetailsCheckExp.apply)(TraderDetailsCheckExp.unapply))
  }

  def declareExp: Form[DeclareExp] = {
    Form(mapping(
      "page" -> text
    )(DeclareExp.apply)(DeclareExp.unapply))
  }

}
