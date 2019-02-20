package model

case class TaxDue(page: String = "tax_due", purchasePrice: Double, customsDuty: Double, importVat: Double, total: Double)
