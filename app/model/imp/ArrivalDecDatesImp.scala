package model.imp

import play.api.mvc.Session

case class ArrivalDecDatesImp(arrivalDay: Int, arrivalMonth: Int, arrivalYear: Int,
                              declarationDay: Int, declarationMonth: Int, declarationYear: Int)

object ArrivalDecDatesImp {

  object Key {
    val ArrivalDay = "arrivalDayImp"
    val ArrivalMonth = "arrivalMonthImp"
    val ArrivalYear = "arrivalYearImp"
    val DeclarationDay = "declarationDayImp"
    val DeclarationMonth = "declarationMonthImp"
    val DeclarationYear = "declarationYearImp"
  }

  def getKeys(): Seq[String] = {
    Seq(Key.ArrivalDay,
        Key.ArrivalMonth,
        Key.ArrivalYear,
        Key.DeclarationDay,
        Key.DeclarationMonth,
        Key.DeclarationYear)
  }

  def fromSession(session: Session): Option[ArrivalDecDatesImp] = {
      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

      def mandatoryInt(name: String): Int = session.get(name).getOrElse("").toInt

    if (optional(Key.ArrivalDay).isEmpty)
      None
    else
      Some(ArrivalDecDatesImp(
        mandatoryInt(Key.ArrivalDay),
        mandatoryInt(Key.ArrivalMonth),
        mandatoryInt(Key.ArrivalYear),
        mandatoryInt(Key.DeclarationDay),
        mandatoryInt(Key.DeclarationMonth),
        mandatoryInt(Key.DeclarationYear)
      ))
  }

  def toSession(page4: ArrivalDecDatesImp): Seq[(String, String)] = {
    Map(
      Key.ArrivalDay -> page4.arrivalDay.toString,
      Key.ArrivalMonth -> page4.arrivalMonth.toString,
      Key.ArrivalYear -> page4.arrivalYear.toString,
      Key.DeclarationDay -> page4.declarationDay.toString,
      Key.DeclarationMonth -> page4.declarationMonth.toString,
      Key.DeclarationYear -> page4.declarationYear.toString

    ).toSeq
  }

}

