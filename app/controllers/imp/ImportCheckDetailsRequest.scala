package controllers.imp

import Service.CountriesService
import config.AppConfig
import controllers.FormsImp.{journeyDetailsImp, taxDueImp, traderDetailsCheckImp}
import controllers.FormsShared.{importExportDate, merchandiseDetails, traderDetails}
import exceptions.MibException
import javax.inject.{Inject, Singleton}
import model.imp.{JourneyDetailsImp, PricesTaxesImp, TaxDueImp, TraderDetailsCheckImp}
import model.shared.{ImportExportDate, MerchandiseDetails, TraderDetails}
import model.{ImportPages, MibTypes}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{AnyContent, Request, Results}
import views.html.importpages.check_details

import scala.concurrent.ExecutionContext

@Singleton
class ImportCheckDetailsRequest @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService)(implicit ec: ExecutionContext, appConfig: AppConfig) extends I18nSupport with Results {

  def post(implicit request: Request[AnyContent]) = {
    Ok
  }

  def get(implicit request: Request[AnyContent]) = {
    val journey = journeyDetailsImp.fill(JourneyDetailsImp.fromSession(request.session).getOrElse(throw new MibException("Journey Details not found")))
    val traderFull = traderDetails.fill(TraderDetails.fromSession(request.session, MibTypes.mibImport).getOrElse(throw new MibException("Trader Details not found"))).get

    val merchandise = merchandiseDetails.fill(MerchandiseDetails.fromSession(request.session, MibTypes.mibImport).getOrElse(throw new MibException("Merchandise Details not found")))
    val traderCheck = traderDetailsCheckImp.fill(TraderDetailsCheckImp(traderFull.getFormattedAddress(traderFull.country.fold("")(countriesService.getCountry(_))), traderFull.vrn, traderFull.vehicleRegNo))
    val arrival = importExportDate.fill(ImportExportDate.fromSession(request.session, MibTypes.mibImport).getOrElse(throw new MibException("ImportExport date Details not found")))
    val prices = PricesTaxesImp.fromSession(request.session).getOrElse(throw new MibException("Prices Details not found"))
    val due = taxDueImp.fill(TaxDueImp(prices.purchasePrice, prices.customsDuty, prices.importVat, prices.customsDuty + prices.importVat))
    Ok(check_details(journey,
                     traderCheck,
                     merchandise,
                     arrival,
                     due,
                     countriesService.getCountries,
                     ImportPages.check_details.case_value,
                     ImportPages.prices_taxes.case_value))
  }

}
