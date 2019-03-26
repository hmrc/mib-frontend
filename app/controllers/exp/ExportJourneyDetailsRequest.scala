package controllers.exp

import service.CountriesService
import config.AppConfig
import controllers.FormsExp._
import controllers.FormsShared._
import exceptions.MibException
import javax.inject.{Inject, Singleton}
import model.exp.JourneyDetailsExp
import model.shared.TraderDetails
import model.{ExportPages, MibTypes}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{AnyContent, Request, Results}
import views.html.exportpages.export_journey_details
import views.html.shared.trader_details

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ExportJourneyDetailsRequest @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService)(implicit ec: ExecutionContext, appConfig: AppConfig) extends I18nSupport with Results {

  def post(implicit request: Request[AnyContent]) = {
    journeyDetailsExp.bindFromRequest().fold(
      formWithErrors => Future.successful(Ok(export_journey_details(
        formWithErrors, countriesService.getCountries, ExportPages.journey_details.toString, ExportPages.export_date.case_value
      ))),
      {
        valueInForm =>
          {
            Future.successful(Ok(trader_details(TraderDetails.fromSession(request.session, MibTypes.mibExport).fold(traderDetails)(traderDetails.fill(_)),
                                                countriesService.getCountries, ExportPages.trader_details.toString,
                                                controllers.routes.ExportController.getExportPage(ExportPages.journey_details.case_value), controllers.routes.ExportController.submitExportPage()
            ))
              .addingToSession(JourneyDetailsExp.toSession(valueInForm): _*))
          }
      }
    )

  }

  def get(implicit request: Request[AnyContent]) = {
    Ok(export_journey_details(journeyDetailsExp.fill(JourneyDetailsExp.fromSession(request.session).getOrElse(throw new MibException("Journey Details not found"))),
                              countriesService.getCountries, ExportPages.journey_details.toString, ExportPages.export_date.case_value))
  }

}
