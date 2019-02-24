package controllers

import model.exp._
import model.imp.{TraderDetailsImp, _}
import play.api.mvc.{RequestHeader, Result}

object ResultWrapper {

  implicit class AdditionalActions(result: Result)(implicit requestHeader: RequestHeader) {

    val keys: Seq[String] = JourneyDetailsImp.getKeys ++
      TraderDetailsImp.getKeys() ++
      ArrivalDecDatesImp.getKeys() ++
      MerchandiseDetailsImp.getKeys ++
      PricesTaxesImp.getKeys ++
      JourneyDetailsExp.getKeys ++
      TraderDetailsExp.getKeys() ++
      DepartureDecDatesExp.getKeys() ++
      MerchandiseDetailsExp.getKeys ++
      PricesTaxesExp.getKeys

    def purgeSession: Result = keys.foldLeft(result) { (result, key) => result.removingFromSession(key) }
  }

}
