package model.exp

import play.api.mvc.Session

case class DepartureDecDatesExp(departureDay: Int, departureMonth: Int, departureYear: Int,
                                declarationDay: Int, declarationMonth: Int, declarationYear: Int)

object DepartureDecDatesExp {

  object Key {
    val DepartureDay = "departureDayExp"
    val DepartureMonth = "departureMonthExp"
    val DepartureYear = "departureYearExp"
    val DeclarationDay = "declarationDayExp"
    val DeclarationMonth = "declarationMonthExp"
    val DeclarationYear = "declarationYearExp"
  }

  def getKeys(): Seq[String] = {
    Seq(Key.DepartureDay,
        Key.DepartureMonth,
        Key.DepartureYear,
        Key.DeclarationDay,
        Key.DeclarationMonth,
        Key.DeclarationYear)
  }

  def fromSession(session: Session): Option[DepartureDecDatesExp] = {
      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

      def mandatoryInt(name: String): Int = session.get(name).getOrElse("").toInt

    if (optional(Key.DepartureDay).isEmpty)
      None
    else
      Some(DepartureDecDatesExp(
        mandatoryInt(Key.DepartureDay),
        mandatoryInt(Key.DepartureMonth),
        mandatoryInt(Key.DepartureYear),
        mandatoryInt(Key.DeclarationDay),
        mandatoryInt(Key.DeclarationMonth),
        mandatoryInt(Key.DeclarationYear)
      ))
  }

  def toSession(page4: DepartureDecDatesExp): Seq[(String, String)] = {
    Map(
      Key.DepartureDay -> page4.departureDay.toString,
      Key.DepartureMonth -> page4.departureMonth.toString,
      Key.DepartureYear -> page4.departureYear.toString,
      Key.DeclarationDay -> page4.declarationDay.toString,
      Key.DeclarationMonth -> page4.declarationMonth.toString,
      Key.DeclarationYear -> page4.declarationYear.toString

    ).toSeq
  }

}

