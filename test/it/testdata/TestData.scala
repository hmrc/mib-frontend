package it.testdata

import model.shared.TraderDetails

object TestData {

  val purchasePriceExample = "125.00"
  val testPortOfEntry = "Port of Colm"
  val testPortOfExit = "Port of ColmExit"
  val testEoriNumber = "GB123456789000"
  val testCountry = "IRL"
  val testCustomsDuty = "22"
  val testImportVat = "22"
  val testVrn = "1234567890"
  val testVehicleRegNo = "1234567890-123456"
  val testUkTraderDetails = TraderDetails(
    trader            = "Tyrion Lannister",
    line1             = None,
    line2             = Some("bottom of the brarrel"),
    city              = Some("Kings Landing"),
    county            = Some("Westerous"),
    postcode          = Some("BN31TE"),
    country           = None,
    vrn               = Some(testVrn),
    vehicleRegNo      = Some(testVehicleRegNo),
    line3             = None,
    buildingAndStreet = Some("Land of fairytales"),
    line2nonuk        = None)
  val testInvoiceNumber = "1234567890"
  val testDescriptionOfGoods = "I am good coming into the uk"

}
