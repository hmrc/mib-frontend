package controllers

import play.api.data.FormError
import play.api.data.format.Formatter
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

object CommonForms {

  val postcodeFormatter: Formatter[String] = new Formatter[String] {

    val ukPostcodePattern = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9]?[A-Za-z]))))\\s?[0-9][A-Za-z]{2})"

    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], String] =
      if (data("country") == "GBR" && !data("postcode").trim().matches(ukPostcodePattern))
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
}
