package model

import enumeratum._

sealed abstract class MibType extends EnumEntry {
  def caseValue: String
}

object MibTypes extends Enum[MibType] {

  case object mibImport extends MibType {
    def caseValue = "mibImport"
  }
  case object mibExport extends MibType {
    def caseValue = "mibExport"
  }

  override def values = findValues
}

