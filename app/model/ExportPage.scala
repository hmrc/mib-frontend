package model

import enumeratum._

sealed abstract class ExportPage extends EnumEntry {
  val case_value: String
}

object ExportPages extends Enum[ExportPage] {

  case object prices extends ExportPage {
    override val case_value: String = "prices"
  }

  case object export_date extends ExportPage {
    override val case_value = "export_date"
  }

  case object journey_details extends ExportPage {
    override val case_value = "journey_details"
  }

  case object trader_details extends ExportPage {
    override val case_value = "trader_details"
  }

  case object merchandise_details extends ExportPage {
    override val case_value = "merchandise_details"
  }

  case object check_details extends ExportPage {
    override val case_value: String = "check_details"
  }

  case object dec_received extends ExportPage {
    override val case_value: String = "dec_received"
  }

  override def values = findValues
}

