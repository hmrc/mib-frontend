package controllers

import Service.{CountriesService, RefService}
import config.AppConfig
import controllers.exp._
import javax.inject.{Inject, Singleton}
import model.ExportPages
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.error_template

import scala.concurrent.ExecutionContext

@Singleton
class ExportController @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService,
                                  exportPricesRequest: ExportPricesRequest, exportDateRequest: ExportDateRequest,
                                  exportJourneyDetails: ExportJourneyDetailsRequest, exportTraderDetailsRequest: ExportTraderDetailsRequest,
                                  exportMerchandiseDetailsRequest: ExportMerchandiseDetailsRequest, exportCheckDetailsRequest: ExportCheckDetailsRequest)
  (implicit ec: ExecutionContext, appConfig: AppConfig, refService: RefService) extends FrontendController with I18nSupport {

  //------------------------------------------------------------------------------------------------------------------------------
  def getExportPage(page: String): Action[AnyContent] = Action { implicit request =>

    page match {

      case ExportPages.prices.case_value              => exportPricesRequest.get

      case ExportPages.export_date.case_value         => exportDateRequest.get

      case ExportPages.journey_details.case_value     => exportJourneyDetails.get

      case ExportPages.trader_details.case_value      => exportTraderDetailsRequest.get

      case ExportPages.merchandise_details.case_value => exportMerchandiseDetailsRequest.get

      case ExportPages.check_details.case_value       => exportCheckDetailsRequest.get

      case _ => {
        Ok(error_template("Merchandise in Baggage", "Merchandise in Baggage", "Page not found"))
      }
    }
  }

  //------------------------------------------------------------------------------------------------------------------------------

  def submitExportPage: Action[AnyContent] = Action { implicit request =>

    val pageno = request.body.asFormUrlEncoded.map(form => {
      val page = form.get("page")
      page.get.head
    })
    pageno match {
      case Some(ExportPages.prices.case_value)              => exportPricesRequest.post

      case Some(ExportPages.export_date.case_value)         => exportDateRequest.post

      case Some(ExportPages.journey_details.case_value)     => exportJourneyDetails.post

      case Some(ExportPages.trader_details.case_value)      => exportTraderDetailsRequest.post

      case Some(ExportPages.merchandise_details.case_value) => exportMerchandiseDetailsRequest.post

      case Some(ExportPages.check_details.case_value)       => exportCheckDetailsRequest.post

      case _ => {
        Ok(error_template("Merchandise in Baggage", "Merchandise in Baggage", "Page not found"))
      }
    }
  }

}
