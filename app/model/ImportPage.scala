package model

import enumeratum._

sealed abstract class ImportPage extends EnumEntry {
  val case_value: String
}

object ImportPages extends Enum[ImportPage] {

  case object journey_details extends ImportPage {
    override val case_value = "journey_details"
  }
  case object trader_details extends ImportPage {
    override val case_value = "trader_details"
  }
  case object merchandise_details extends ImportPage {
    override val case_value = "merchandise_details"
  }
  case object arrivaldec_dates extends ImportPage {
    override val case_value = "arrivaldec_dates"
  }
  case object prices_taxes extends ImportPage {
    override val case_value = "prices_taxes"
  }
  case object check_details extends ImportPage {
    override val case_value: String = "check_details"
  }
  case object tax_due extends ImportPage {
    override val case_value: String = "tax_due"
  }
  case object declare extends ImportPage {
    override val case_value: String = "declare"
  }

  override def values = findValues
}

