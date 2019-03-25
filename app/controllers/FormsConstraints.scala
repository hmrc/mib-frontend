package controllers

import java.time.temporal.ChronoUnit.DAYS
import java.time.{LocalDate, ZoneId}

import model.imp.{PricesTaxesImp, TaxDueImp}
import model.shared.{ImportExportDate, TraderDetails}
import model.{MibType, MibTypes, YesNoValues}
import play.api.data.format.Formatter
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}
import play.api.data.{Form, FormError}
import service.WorkingDaysService

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

  def countryConstraint = Constraint[String]("constraint.country") { o =>
    if (o == null) Invalid(ValidationError("error.required.country")) else if (o.trim.isEmpty) Invalid(ValidationError("error.required.country")) else Valid
  }

  def priceConstraint = Constraint[Double]("constraint.price") { o =>
    val bigDec = BigDecimal(o).setScale(2, BigDecimal.RoundingMode.HALF_UP)

    if (bigDec <= 900)
      Valid
    else
      Invalid(ValidationError("error.max.purchase.value"))
  }

  def validDate(form: Form[ImportExportDate], isImport: Boolean, workingDaysService: WorkingDaysService) = {

    val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
    val importDt: LocalDate = format.parse(form("importExportYear").value.get + "-" + form("importExportMonth").value.get +
      "-" + form("importExportDay").value.get).toInstant.atZone(ZoneId.systemDefault).toLocalDate
    val currentDt: LocalDate = java.time.LocalDate.now
    val importExportDate: ImportExportDate = ImportExportDate(form("importExportDay").value.get.toInt, form("importExportMonth").value.get.toInt, form("importExportYear").value.get.toInt)
    val fiveWorkingDays = workingDaysService.addWorkingDays(currentDt, 5)

    if (!(importExportDate.isValidDate)) {
      form.withError("", "error.real.date")
    } else {
      DAYS.between(currentDt, importDt) match {
        case x if (0 <= x && importDt.isBefore(fiveWorkingDays)) => form
        case x if (x < 0) => form.withError("", if (isImport) {
          "error.import.date.in.past"
        } else {
          "error.export.date.in.past"
        })
        case _ => form.withError("", if (isImport) {
          "error.import.date"
        } else {
          "error.export.date"
        })
      }
    }

  }

  def checkTotalTax(form: Form[PricesTaxesImp], purchasePrice: Double): Form[PricesTaxesImp] = {

    if ((form("customsDuty").value.get.toDouble + form("importVat").value.get.toDouble) > purchasePrice) {
      form.withError("", "error.tax.amount", purchasePrice.formatted("%,1.2f").toString)
    } else {
      form
    }
  }

  def setImportExportDateSummaryError(form: Form[ImportExportDate], mibType: MibType) = {

    val all = form("importExportYear").value.get + form("importExportMonth").value.get + form("importExportDay").value.get

    all.length match {
      case 0 => form.withError("", "error.real.date")
      case _ => if (mibType == MibTypes.mibImport) form.withError("", "error.invalid.import.date") else form.withError("", "error.invalid.export.date")
    }

  }

  def validateTraderDetailsNoPostCodeOrCountry(form: Form[TraderDetails]) = {

    if (form("uk").value.get == YesNoValues.yes) {
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

    if (form("uk").value.get == YesNoValues.yes) {
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

  def validateTraderDetailsNoTrader(form: Form[TraderDetails]) = {

    if (form("trader").value.isDefined && form("trader").value.get.length > 0) {
      form
    } else {
      form.withError("trader", "error.required.trader")
    }

  }

}
