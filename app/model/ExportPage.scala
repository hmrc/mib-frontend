package model

import enumeratum._

sealed abstract class ExportPage extends EnumEntry {
  val case_value: String
}

object ExportPages extends Enum[ExportPage] {

  case object journey_details extends ExportPage {
    override val case_value = "journey_details"
  }
  case object trader_details extends ExportPage {
    override val case_value = "trader_details"
  }
  case object merchandise_details extends ExportPage {
    override val case_value = "merchandise_details"
  }
  case object departuredec_dates extends ExportPage {
    override val case_value = "departuredec_dates"
  }
  case object prices_taxes extends ExportPage {
    override val case_value = "prices_taxes"
  }
  case object check_details extends ExportPage {
    override val case_value: String = "check_details"
  }
  case object declare extends ExportPage {
    override val case_value: String = "declare"
  }

  override def values = findValues
}

