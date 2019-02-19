package model

import play.api.mvc.Session

case class ArrivalDecDates(page: String = "arrivaldec_dates", arrivalDay: Int, arrivalMonth: Int, arrivalYear: Int,
                           declarationDay: Int, declarationMonth: Int, declarationYear: Int)

object ArrivalDecDates {

  object Key {
    val Page = "page"
    val ArrivalDay = "arrivalDay"
    val ArrivalMonth = "arrivalMonth"
    val ArrivalYear = "arrivalYear"
    val DeclarationDay = "declarationDay"
    val DeclarationMonth = "declarationMonth"
    val DeclarationYear = "declarationYear"
  }

  def getKeys(): Seq[String] = {
    Seq(Key.ArrivalDay,
        Key.ArrivalMonth,
        Key.ArrivalYear,
        Key.DeclarationDay,
        Key.DeclarationMonth,
        Key.DeclarationYear,
        Key.Page)
  }

  def fromSession(session: Session): Option[ArrivalDecDates] = {
      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

      def mandatoryInt(name: String): Int = session.get(name).getOrElse("").toInt

    if (optional(Key.ArrivalDay).isEmpty)
      None
    else
      Some(ArrivalDecDates(
        mandatory(Key.Page),
        mandatoryInt(Key.ArrivalDay),
        mandatoryInt(Key.ArrivalMonth),
        mandatoryInt(Key.ArrivalYear),
        mandatoryInt(Key.DeclarationDay),
        mandatoryInt(Key.DeclarationMonth),
        mandatoryInt(Key.DeclarationYear)
      ))
  }

  def toSession(page4: ArrivalDecDates): Seq[(String, String)] = {
    Map(
      Key.Page -> page4.page,
      Key.ArrivalDay -> page4.arrivalDay.toString,
      Key.ArrivalMonth -> page4.arrivalMonth.toString,
      Key.ArrivalYear -> page4.arrivalYear.toString,
      Key.DeclarationDay -> page4.declarationDay.toString,
      Key.DeclarationMonth -> page4.declarationMonth.toString,
      Key.DeclarationYear -> page4.declarationYear.toString

    ).toSeq
  }

}

