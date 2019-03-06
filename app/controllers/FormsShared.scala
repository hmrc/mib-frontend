package controllers

import model.SelectDecType
import model.shared.{ImportExportDate, MerchandiseDetails, Prices, TraderDetails}
import play.api.data.Form
import play.api.data.Forms.{mapping, number, of, optional, text}
import play.api.data.format.Formats.doubleFormat
import play.api.data.validation.Constraints.maxLength

object FormsShared {

  def prices: Form[Prices] = {
    Form(mapping(
      "purchasePrice" -> of(doubleFormat).verifying(FormsConstraints.priceConstraint)
    )(Prices.apply)(Prices.unapply))
  }

  val selectDecType: Form[SelectDecType] = {
    Form(mapping(
      "select_dec_type" -> optional(text).verifying("select.dec.type.error", _.nonEmpty))(SelectDecType.apply)(SelectDecType.unapply))
  }

  //TODO need better validation
  def importExportDate: Form[ImportExportDate] = {
    Form(mapping(
      "importExportDay" -> number,
      "importExportMonth" -> number,
      "importExportYear" -> number
    )(ImportExportDate.apply)(ImportExportDate.unapply))
  }

  def traderDetails: Form[TraderDetails] = {
    Form(mapping(
      "uk" -> text,
      "trader" -> text.transform[String](_.trim, identity).verifying(maxLength(100)),
      "line1" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(100))),
      "line2" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(100))),
      "city" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(60))),
      "county" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(60))),
      "postcode" -> optional(of(FormsConstraints.postcodeFormatter)),
      "country" -> optional(text.verifying(FormsConstraints.countryConstraint)),
      "vrn" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(60))),
      "vehicleRegNo" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(60))),
      "line3" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(100))),
      "buildingAndStreet" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(100))),
      "line2nonuk" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(100)))
    )(TraderDetails.apply)(TraderDetails.unapply))
  }

  def merchandiseDetails: Form[MerchandiseDetails] = {
    Form(mapping(
      "invoiceNumber" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(20))),
      "desciptionOfGoods" -> text.transform[String](_.trim, identity).verifying("error.invalid.desciptionOfGoods", s => s.length > 0).verifying(maxLength(1200))
    )(MerchandiseDetails.apply)(MerchandiseDetails.unapply))
  }

}
