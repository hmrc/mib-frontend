package controllers.imp

import Service.CountriesService
import config.AppConfig
import controllers.FormsImp.journeyDetailsImp
import controllers.FormsShared.{importExportDate, prices, traderDetails}
import exceptions.MibException
import model.{ImportPages, MibTypes}
import model.shared.{ImportExportDate, Prices, TraderDetails}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{AnyContent, Request, Results}
import views.html.shared.{import_export_date, purchase_prices, trader_details}

import scala.concurrent.ExecutionContext
import javax.inject.{Inject, Singleton}
import model.imp.JourneyDetailsImp
import views.html.importpages.journey_details

@Singleton
class ImportJourneyDetailsRequest @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService)(implicit ec: ExecutionContext, appConfig: AppConfig) extends I18nSupport with Results {

  def post(implicit request: Request[AnyContent]) = {

    journeyDetailsImp.bindFromRequest().fold(
      formWithErrors => Ok(journey_details(
        formWithErrors, countriesService.getCountries,
        ImportPages.journey_details.case_value,
        ImportPages.tax_due.case_value
      )),
      {
        valueInForm =>
          {
            Ok(trader_details(TraderDetails.fromSession(request.session, MibTypes.mibImport).fold(traderDetails)(traderDetails.fill(_)),
                              countriesService.getCountries, ImportPages.trader_details.case_value,
                              controllers.routes.ImportController.getImportPage(ImportPages.journey_details.case_value), controllers.routes.ImportController.submitImportPage()))
              .addingToSession(JourneyDetailsImp.toSession(valueInForm): _*)
          }
      }
    )

  }
  def get(implicit request: Request[AnyContent]) = {
    Ok(journey_details(journeyDetailsImp.fill(JourneyDetailsImp.fromSession(request.session).getOrElse(throw new MibException("Journey Details not found"))),
                       countriesService.getCountries, ImportPages.journey_details.case_value, ImportPages.tax_due.case_value))
  }
}
