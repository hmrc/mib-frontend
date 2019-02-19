package controllers

import model._
import play.api.Logger
import play.api.data.Forms.{optional, _}
import play.api.data._
import play.api.data.format.Formatter
import play.api.data.validation.Constraints._
import play.api.data.validation._
import play.api.data.format.Formats._

object Forms {

  val postcodeFormatter: Formatter[String] = new Formatter[String] {

    val ukPostcodePattern = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9]?[A-Za-z]))))\\s?[0-9][A-Za-z]{2})"

    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], String] =
      if (data("country") == "GBR" && !data("postcode").trim().matches(ukPostcodePattern))
        Left(Seq(FormError("postcode", "error.invalid.postcode")))
      else
        Right(data("postcode").trim())

    override def unbind(key: String, value: String): Map[String, String] = Map(key -> value)

  }

  val selectDecType: Form[SelectDecType] = {
    Form(mapping(
      "select_dec_type" -> optional(text).verifying("select.dec.type.error", _.nonEmpty))(SelectDecType.apply)(SelectDecType.unapply))
  }

  def journeyDetails: Form[JourneyDetails] = {
    Form(mapping(
      "page" -> text,
      "portOfEntry" -> text.transform[String](_.trim, identity).verifying("error.invalid.portOfEntry", s => s.length > 0).verifying(maxLength(100)),
      "countryOfOrigin" -> text.transform[String](_.trim, identity).verifying("error.invalid.countryOfOrigin", s => s.length > 0).verifying(maxLength(100)),
      "eori" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(20)))
    )(JourneyDetails.apply)(JourneyDetails.unapply))
  }

  def traderDetails: Form[TraderDetails] = {
    Form(mapping(
      "page" -> text,
      "trader" -> text.transform[String](_.trim, identity).verifying(maxLength(100)),
      "line1" -> text.transform[String](_.trim, identity).verifying("error.invalid.addressline1", s => s.length > 0)
        .verifying(maxLength(100)),
      "line2" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(100))),
      "city" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(60))),
      "county" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(60))),
      "postcode" -> of(postcodeFormatter),
      "country" -> text.verifying(LocalConstraint.countryConstraint),
      "vrn" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(60))),
      "vehicleRegNo" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(60)))
    )(TraderDetails.apply)(TraderDetails.unapply))
  }

  def merchandiseDetails: Form[MerchandiseDetails] = {
    Form(mapping(
      "page" -> text,
      "invoiceNumber" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(20))),
      "desciptionOfGoods" -> text.transform[String](_.trim, identity).verifying("error.invalid.desciptionOfGoods", s => s.length > 0).verifying(maxLength(1200))
    )(MerchandiseDetails.apply)(MerchandiseDetails.unapply))
  }

  //TODO need better validation
  def arrivalDecDates: Form[ArrivalDecDates] = {
    Form(mapping(
      "page" -> text,
      "arrivalDay" -> number(min = 1, max = 31),
      "arrivalMonth" -> number(min = 1, max = 12),
      "arrivalYear" -> number(min = 1900, max = 2100),
      "declarationDay" -> number(min = 1, max = 31),
      "declarationMonth" -> number(min = 1, max = 12),
      "declarationYear" -> number(min = 1900, max = 2100)
    )(ArrivalDecDates.apply)(ArrivalDecDates.unapply))
  }

  def pricesTaxes: Form[PricesTaxes] = {
    Form(mapping(
      "page" -> text,
      "purchasePrice" -> of(doubleFormat),
      "customsDuty" -> of(doubleFormat),
      "importVat" -> of(doubleFormat)
    )(PricesTaxes.apply)(PricesTaxes.unapply))
  }

  def traderDetailsCheck: Form[TraderDetailsCheck] = {
    Form(mapping(
      "nameAddress" -> text,
      "vrn" -> optional(text),
      "vehicleRegNo" -> optional(text)
    )(TraderDetailsCheck.apply)(TraderDetailsCheck.unapply))
  }

  object LocalConstraint {

    def countryConstraint = Constraint[String]("constraint.country") { o =>
      if (o == null) Invalid(ValidationError("error.required.country")) else if (o.trim.isEmpty) Invalid(ValidationError("error.required.country")) else Valid
    }

  }

}
