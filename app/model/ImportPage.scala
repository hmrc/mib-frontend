package model

import enumeratum._

sealed abstract class ImportPage extends EnumEntry {
  val case_value: String
}

object ImportPages extends Enum[ImportPage] {

  case object prices extends ImportPage {
    override val case_value: String = "prices"
  }
  case object import_date extends ImportPage {
    override val case_value = "import_date"
  }
  case object journey_details extends ImportPage {
    override val case_value = "journey_details"
  }
  case object trader_details extends ImportPage {
    override val case_value = "trader_details"
  }
  case object merchandise_details extends ImportPage {
    override val case_value = "merchandise_details"
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

  case object payment_received extends ImportPage {
    override val case_value: String = "payment_received"
  }

  override def values = findValues
}

