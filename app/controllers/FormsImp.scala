package controllers

import model.imp._
import play.api.data.Forms.{optional, _}
import play.api.data._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints._

object FormsImp {

  def journeyDetailsImp: Form[JourneyDetailsImp] = {
    Form(mapping(
      "portOfEntry" -> text.transform[String](_.trim, identity).verifying("error.invalid.portOfEntry", s => s.length > 0).verifying(maxLength(100)),
      "countryOfOrigin" -> text.transform[String](_.trim, identity).verifying("error.invalid.countryOfOrigin", s => s.length > 0).verifying(maxLength(100)),
      "eori" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(20)))
    )(JourneyDetailsImp.apply)(JourneyDetailsImp.unapply))
  }

  def pricesTaxesImp: Form[PricesTaxesImp] = {
    Form(mapping(
      "customsDuty" -> of(doubleFormat),
      "importVat" -> of(doubleFormat)
    )(PricesTaxesImp.apply)(PricesTaxesImp.unapply))
  }

  def taxDueImp: Form[TaxDueImp] =
    Form(mapping(
      "purchasePrice" -> of(doubleFormat),
      "customsDuty" -> of(doubleFormat),
      "importVat" -> of(doubleFormat),
      "total" -> of(doubleFormat)
    ) (TaxDueImp.apply)(TaxDueImp.unapply))

  def traderDetailsCheckImp: Form[TraderDetailsCheckImp] = {
    Form(mapping(
      "nameAddress" -> text,
      "vrn" -> optional(text),
      "vehicleRegNo" -> optional(text)
    )(TraderDetailsCheckImp.apply)(TraderDetailsCheckImp.unapply))
  }

}
