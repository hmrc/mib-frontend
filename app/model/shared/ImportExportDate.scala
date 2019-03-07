package model.shared

import java.time.{LocalDate, ZoneId}

import model.MibType
import play.api.mvc.Session

final case class ImportExportDate(importExportDay: Int, importExportMonth: Int, importExportYear: Int) {

  def isValidDate: Boolean = {

    val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
    val localDateYear: LocalDate = format.parse(importExportYear.toString + "-1-1").toInstant.atZone(ZoneId.systemDefault).toLocalDate

    (importExportDay, importExportMonth, importExportYear) match {
      case month if (month._2 < 1) => false
      case month if (month._2 > 12) => false
      case day if (day._1 < 1) => false
      case day if ((day._2 == 1) && (day._1 > 31)) => false
      case day if ((day._2 == 3) && (day._1 > 31)) => false
      case day if ((day._2 == 4) && (day._1 > 30)) => false
      case day if ((day._2 == 5) && (day._1 > 31)) => false
      case day if ((day._2 == 6) && (day._1 > 30)) => false
      case day if ((day._2 == 7) && (day._1 > 31)) => false
      case day if ((day._2 == 8) && (day._1 > 31)) => false
      case day if ((day._2 == 9) && (day._1 > 30)) => false
      case day if ((day._2 == 10) && (day._1 > 31)) => false
      case day if ((day._2 == 11) && (day._1 > 30)) => false
      case day if ((day._2 == 12) && (day._1 > 31)) => false
      case leapyear if ((leapyear._2 == 2) && (leapyear._1 > 29) && (localDateYear.isLeapYear)) => false
      case leapyear if ((leapyear._2 == 2) && (leapyear._1 > 28) && (!(localDateYear.isLeapYear))) => false
      case _ => true
    }
  }
}

object ImportExportDate extends Shared {

  object Key {
    val ImportExportDay = "importExportDay"
    val ImportExportMonth = "importExportMonth"
    val ImportExportYear = "importExportYear"
  }

  def getKeys(mibType: MibType): Seq[String] = {

    Seq(Key.ImportExportDay + appendVal(mibType),
      Key.ImportExportMonth + appendVal(mibType),
      Key.ImportExportYear + appendVal(mibType))
  }

  def fromSession(session: Session, mibType: MibType): Option[ImportExportDate] = {
      def optional(name: String): Option[String] = session.get(name) match {
        case Some("") => None
        case n        => n
      }

      def mandatory(name: String): String = session.get(name).getOrElse("")

      def mandatoryInt(name: String): Int = session.get(name).getOrElse("").toInt

    if (optional(Key.ImportExportDay + appendVal(mibType)).isEmpty)
      None
    else
      Some(ImportExportDate(
        mandatoryInt(Key.ImportExportDay + appendVal(mibType)),
        mandatoryInt(Key.ImportExportMonth + appendVal(mibType)),
        mandatoryInt(Key.ImportExportYear + appendVal(mibType))
      ))
  }

  def toSession(page: ImportExportDate, mibType: MibType): Seq[(String, String)] = {
    Map(
      Key.ImportExportDay + appendVal(mibType) -> page.importExportDay.toString,
      Key.ImportExportMonth + appendVal(mibType) -> page.importExportMonth.toString,
      Key.ImportExportYear + appendVal(mibType) -> page.importExportYear.toString
    ).toSeq
  }

}

