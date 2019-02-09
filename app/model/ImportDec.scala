package model

final case class ImportDec(portOfEntry: String, countryOfOrigin: String, eori: Option[String])
