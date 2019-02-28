package model.shared

import model.MibType
import play.api.mvc.Session

case class ImportExportDate(importExportDay: Int, importExportMonth: Int, importExportYear: Int)

object ImportExportDate extends Shared {

  object Key {
    val ImportExportDay = "importExportDay"
    val ImportExportMonth = "importExportMonth"
    val ImportExportYear = "importExportYearImp"
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

