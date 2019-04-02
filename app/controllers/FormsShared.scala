package controllers

import model.SelectDecType
import model.shared.{ImportExportDate, MerchandiseDetails, Prices, TraderDetails}
import play.api.data.Form
import play.api.data.Forms.{mapping, number, of, optional, text}
import play.api.data.format.Formats.doubleFormat
import play.api.data.validation.Constraints.maxLength
import model.{MibType, MibTypes}

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
      "trader" -> text.transform[String](_.trim, identity).verifying(maxLength(100))
        .verifying(FormsConstraints.emojiConstraint("trader", "error.invalid.char")),
      "line1" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(100))
        .verifying(FormsConstraints.emojiConstraint("line1", "error.invalid.char"))),
      "line2" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(100))
        .verifying(FormsConstraints.emojiConstraint("line2", "error.invalid.char"))),
      "city" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(60))
        .verifying(FormsConstraints.emojiConstraint("city", "error.invalid.char"))),
      "county" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(60))
        .verifying(FormsConstraints.emojiConstraint("county", "error.invalid.char"))),
      "postcode" -> optional(of(FormsConstraints.postcodeFormatter)),
      "country" -> optional(text.verifying(FormsConstraints.countryConstraint)),
      "vrn" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(60))),
      "vehicleRegNo" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(60))),
      "line3" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(100)).verifying(FormsConstraints.emojiConstraint("line3", "error.invalid.char"))),
      "buildingAndStreet" -> optional(text.transform[String](_.trim, identity)
        .verifying(maxLength(100)).verifying(FormsConstraints.emojiConstraint("buildingAndStreet", "error.invalid.char"))),
      "line2nonuk" -> optional(text.transform[String](_.trim, identity)
        .verifying(maxLength(100)).verifying(FormsConstraints.emojiConstraint("line2nonuk", "error.invalid.char")))
    )(TraderDetails.apply)(TraderDetails.unapply))
  }

  def merchandiseDetails(mibType: MibType): Form[MerchandiseDetails] = {

    //MibTypes.mibImport
    //MibTypes.mibExport
    val errorMessage = if (mibType == MibTypes.mibImport) "error.invalid.descriptionOfGoods" else "error.invalid.descriptionOfGoodsExp"

    Form(mapping(
      "invoiceNumber" -> optional(text.transform[String](_.trim, identity).verifying(maxLength(20))),
      "desciptionOfGoods" -> text.transform[String](_.trim, identity).verifying(errorMessage, s => s.length > 0).verifying(maxLength(1200))
    )(MerchandiseDetails.apply)(MerchandiseDetails.unapply))
  }

}

