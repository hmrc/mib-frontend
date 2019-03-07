package controllers.imp

import Service.CountriesService
import config.AppConfig
import controllers.FormsImp.{journeyDetailsImp, taxDueImp}
import exceptions.MibException
import javax.inject.{Inject, Singleton}
import model.{ImportPages, MibTypes}
import model.imp.{JourneyDetailsImp, PricesTaxesImp, TaxDueImp}
import model.shared.Prices
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{AnyContent, Request, Results}
import views.html.importpages.{journey_details, tax_due}

import scala.concurrent.ExecutionContext

@Singleton
class TaxDueRequest @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService)(implicit ec: ExecutionContext, appConfig: AppConfig) extends I18nSupport with Results {

  def post(implicit request: Request[AnyContent]) = {
    Ok(journey_details(JourneyDetailsImp.fromSession(request.session).fold(journeyDetailsImp)(journeyDetailsImp.fill(_)),
                       countriesService.getCountries, ImportPages.journey_details.case_value, ImportPages.tax_due.case_value))
  }

  def get(implicit request: Request[AnyContent]) = {
    val prices = PricesTaxesImp.fromSession(request.session).getOrElse(throw new MibException("Prices Details not found"))
    val pricesVal = Prices.fromSession(request.session, MibTypes.mibImport).getOrElse(throw new MibException("Prices details not found"))
    val due = TaxDueImp(pricesVal.purchasePrice, prices.customsDuty, prices.importVat, prices.customsDuty + prices.importVat)
    Ok(tax_due(taxDueImp.fill(due),
               ImportPages.tax_due.case_value, ImportPages.prices_taxes.case_value))
  }
}
