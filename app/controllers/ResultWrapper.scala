package controllers

import model.MibTypes
import model.exp._
import model.imp._
import model.shared._
import play.api.mvc.{RequestHeader, Result}

object ResultWrapper {

  implicit class AdditionalActions(result: Result)(implicit requestHeader: RequestHeader) {

    val keys: Seq[String] =
      Prices.getKeys(MibTypes.mibImport) ++
        Prices.getKeys(MibTypes.mibExport) ++
        ImportExportDate.getKeys(MibTypes.mibImport) ++
        ImportExportDate.getKeys(MibTypes.mibExport) ++
        JourneyDetailsImp.getKeys ++
        TraderDetails.getKeys(MibTypes.mibImport) ++
        TraderDetails.getKeys(MibTypes.mibExport) ++
        MerchandiseDetails.getKeys(MibTypes.mibImport) ++
        MerchandiseDetails.getKeys(MibTypes.mibExport) ++
        PricesTaxesImp.getKeys ++
        JourneyDetailsExp.getKeys ++
        DeclarationReceived.getKeys()

    def purgeSession: Result = keys.foldLeft(result) { (result, key) => result.removingFromSession(key) }
  }

}
