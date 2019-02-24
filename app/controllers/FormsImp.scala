package controllers

import model._
import model.imp.{TraderDetailsImp, _}
import play.api.data.Forms.{optional, _}
import play.api.data._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints._

object FormsImp {

  val selectDecType: Form[SelectDecType] = {
    Form(mapping(
      "select_dec_type" -> optional(text).verifying("select.dec.type.error", _.nonEmpty))(SelectDecType.apply)(SelectDecType.unapply))
  }

  def journeyDetailsImp: Form[JourneyDetailsImp] = {
    Form(mapping(
      "portOfEntry" -> text.transform[String](_.trim, identity).verifying("error.invalid.portOfEntry", s => s.length > 0).verifying(maxLength(100)),
      "countryOfOrigin" -> text.transform[String](_.trim, identity).verifying("error.invalid.countryOfOrigin", s => s.length > 0).verifying(maxLength(100)),
      "eori" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(20)))
    )(JourneyDetailsImp.apply)(JourneyDetailsImp.unapply))
  }

  def traderDetailsImp: Form[TraderDetailsImp] = {
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
    )(TraderDetailsImp.apply)(TraderDetailsImp.unapply))
  }
  def merchandiseDetailsImp: Form[MerchandiseDetailsImp] = {
    Form(mapping(
      "invoiceNumber" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(20))),
      "desciptionOfGoods" -> text.transform[String](_.trim, identity).verifying("error.invalid.desciptionOfGoods", s => s.length > 0).verifying(maxLength(1200))
    )(MerchandiseDetailsImp.apply)(MerchandiseDetailsImp.unapply))
  }

  //TODO need better validation
  def arrivalDecDatesImp: Form[ArrivalDecDatesImp] = {
    Form(mapping(
      "arrivalDay" -> number(min = 1, max = 31),
      "arrivalMonth" -> number(min = 1, max = 12),
      "arrivalYear" -> number(min = 1900, max = 2100),
      "declarationDay" -> number(min = 1, max = 31),
      "declarationMonth" -> number(min = 1, max = 12),
      "declarationYear" -> number(min = 1900, max = 2100)
    )(ArrivalDecDatesImp.apply)(ArrivalDecDatesImp.unapply))
  }

  def pricesTaxesImp: Form[PricesTaxesImp] = {
    Form(mapping(
      "purchasePrice" -> of(doubleFormat),
      "customsDuty" -> of(doubleFormat),
      "importVat" -> of(doubleFormat)
    )(PricesTaxesImp.apply)(PricesTaxesImp.unapply))
  }

  def taxDueImp: Form[TaxDueImp] = {
    Form(mapping(
      "purchasePrice" -> of(doubleFormat),
      "customsDuty" -> of(doubleFormat),
      "importVat" -> of(doubleFormat),
      "total" -> of(doubleFormat)
    )(TaxDueImp.apply)(TaxDueImp.unapply))
  }

  def traderDetailsCheckImp: Form[TraderDetailsCheckImp] = {
    Form(mapping(
      "nameAddress" -> text,
      "vrn" -> optional(text),
      "vehicleRegNo" -> optional(text)
    )(TraderDetailsCheckImp.apply)(TraderDetailsCheckImp.unapply))
  }

  def declareImp: Form[DeclareImp] = {
    Form(mapping(
      "page" -> text
    )(DeclareImp.apply)(DeclareImp.unapply))
  }

}
