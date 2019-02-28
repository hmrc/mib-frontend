package controllers

import model.shared.TraderDetails
import play.api.Logger
import play.api.data.{Form, FormError}
import play.api.data.format.Formatter
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

object FormsConstraints {

  val postcodeFormatter: Formatter[String] = new Formatter[String] {

    val ukPostcodePattern = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9]?[A-Za-z]))))\\s?[0-9][A-Za-z]{2})"

    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], String] =
      if (!data("postcode").trim().matches(ukPostcodePattern))
        Left(Seq(FormError("postcode", "error.invalid.postcode")))
      else
        Right(data("postcode").trim())

    override def unbind(key: String, value: String): Map[String, String] = Map(key -> value)

  }

  object LocalConstraint {

    def countryConstraint = Constraint[String]("constraint.country") { o =>
      if (o == null) Invalid(ValidationError("error.required.country")) else if (o.trim.isEmpty) Invalid(ValidationError("error.required.country")) else Valid
    }

  }

  def validateTraderDetailsNoPostCodeOrCountry(form: Form[TraderDetails]) = {

    if (form("uk").value.get == "Yes") {
      if (form("postcode").value.isDefined) {
        form
      } else {
        form.withError("postcode", "error.invalid.postcode")
      }
    } else {
      if (form("country").value.isDefined) {
        form
      } else {
        form.withError("country", "error.required.country")
      }
    }

  }

  def validateTraderDetailsNoLine1(form: Form[TraderDetails]) = {

    if (form("uk").value.get == "Yes") {
      if (form("buildingAndStreet").value.isDefined) {
        form
      } else {
        form.withError("buildingAndStreet", "error.invalid.addressline1")
      }
    } else {
      if (form("line1").value.isDefined) {
        form
      } else {
        form.withError("line1", "error.invalid.addressline1")
      }
    }

  }

}
