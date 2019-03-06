package controllers

import Service.CountriesService
import config.AppConfig
import controllers.imp._
import javax.inject.{Inject, Singleton}
import model.ImportPages
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.error_template

import scala.concurrent.ExecutionContext

@Singleton
class ImportController @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService,
                                  pricesRequest: ImportPricesRequest, importDateRequest: ImportDateRequest,
                                  pricesTaxesRequest: PricesTaxesRequest, importJourneyDetailsRequest: ImportJourneyDetailsRequest,
                                  importTraderDetailsRequest: ImportTraderDetailsRequest, importMerchandiseDetails: ImportMerchandiseDetails,
                                  taxDueRequest: TaxDueRequest, importCheckDetailsRequest: ImportCheckDetailsRequest)
  (implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendController with I18nSupport {

  //------------------------------------------------------------------------------------------------------------------------------
  def getImportPage(page: String): Action[AnyContent] = Action { implicit request =>

    page match {

      case ImportPages.prices.case_value              => pricesRequest.get

      case ImportPages.import_date.case_value         => importDateRequest.get

      case ImportPages.prices_taxes.case_value        => pricesTaxesRequest.get

      case ImportPages.tax_due.case_value             => taxDueRequest.get

      case ImportPages.journey_details.case_value     => importJourneyDetailsRequest.get

      case ImportPages.trader_details.case_value      => importTraderDetailsRequest.get

      case ImportPages.merchandise_details.case_value => importMerchandiseDetails.get

      case ImportPages.check_details.case_value       => importCheckDetailsRequest.get

      case _ => {
        Ok(error_template("Merchandise in Baggage", "Merchandise in Baggage", "Page not found"))
      }

    }
  }

  //------------------------------------------------------------------------------------------------------------------------------

  def submitImportPage: Action[AnyContent] = Action { implicit request =>

    val pageno = request.body.asFormUrlEncoded.map(form => {
      val page = form.get("page")
      page.get.head
    })
    pageno match {

      case Some(ImportPages.prices.case_value)              => pricesRequest.post

      case Some(ImportPages.import_date.case_value)         => importDateRequest.post

      case Some(ImportPages.prices_taxes.case_value)        => pricesTaxesRequest.post

      case Some(ImportPages.tax_due.case_value)             => taxDueRequest.post

      case Some(ImportPages.journey_details.case_value)     => importJourneyDetailsRequest.post

      case Some(ImportPages.trader_details.case_value)      => importTraderDetailsRequest.post

      case Some(ImportPages.merchandise_details.case_value) => importMerchandiseDetails.post

      case Some(ImportPages.check_details.case_value)       => importCheckDetailsRequest.post

      case _ => {
        Ok(error_template("Merchandise in Baggage", "Merchandise in Baggage", "Page not found"))
      }
    }
  }

}
