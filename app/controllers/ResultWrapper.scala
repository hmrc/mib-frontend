package controllers
import model._
import play.api.mvc.{RequestHeader, Result}

object ResultWrapper {

  implicit class AdditionalActions(result: Result)(implicit requestHeader: RequestHeader) {

    val keys: Seq[String] = JourneyDetails.getKeys ++ ArrivalDecDates.getKeys() ++ MerchandiseDetails.getKeys ++ PricesTaxes.getKeys ++ TraderDetails.getKeys()
    def purgeSession: Result = keys.foldLeft(result) { (result, key) => result.removingFromSession(key) }
  }
}
