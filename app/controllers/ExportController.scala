package controllers

import Service.CountriesService
import config.AppConfig
import controllers.FormsExp._
import javax.inject.{Inject, Singleton}
import model.ExportPages
import model.exp._
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.exportpages._

import scala.concurrent.ExecutionContext

@Singleton
class ExportController @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService)
  (implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendController with I18nSupport {

  //------------------------------------------------------------------------------------------------------------------------------
  def getExportPage(page: String): Action[AnyContent] = Action { implicit request =>
    page match {
      case ExportPages.journey_details.case_value => Ok(export_journey_details(journeyDetailsExp.fill(JourneyDetailsExp.fromSession(request.session).get),
                                                                               countriesService.getCountries, ExportPages.journey_details.toString))
      case ExportPages.trader_details.case_value =>
        Ok(export_trader_details(traderDetailsExp.fill(TraderDetailsExp.fromSession(request.session).get),
                                 countriesService.getCountries, ExportPages.trader_details.toString, ExportPages.journey_details.toString))

      case ExportPages.merchandise_details.case_value =>
        Ok(export_merchandise_details(merchandiseDetailsExp.fill(MerchandiseDetailsExp.fromSession(request.session).get),
                                      ExportPages.merchandise_details.toString, ExportPages.trader_details.toString))

      case ExportPages.departuredec_dates.case_value =>
        Ok(export_departuredec_dates(departureDecDatesExp.fill(DepartureDecDatesExp.fromSession(request.session).get),
                                     ExportPages.departuredec_dates.toString, ExportPages.merchandise_details.toString))

      case ExportPages.prices_taxes.case_value =>
        Ok(export_prices_taxes(pricesTaxesExp.fill(PricesTaxesExp.fromSession(request.session).get),
                               ExportPages.prices_taxes.toString, ExportPages.departuredec_dates.toString))

      case ExportPages.check_details.case_value => {
        val journey = journeyDetailsExp.fill(JourneyDetailsExp.fromSession(request.session).get)
        val traderFull = traderDetailsExp.fill(TraderDetailsExp.fromSession(request.session).get).get
        val merchandise = merchandiseDetailsExp.fill(MerchandiseDetailsExp.fromSession(request.session).get)
        val traderCheck = traderDetailsCheckExp.fill(TraderDetailsCheckExp(traderFull.getFormattedAddress(countriesService.getCountry(traderFull.country)), traderFull.vrn, traderFull.vehicleRegNo))
        val departure = departureDecDatesExp.fill(DepartureDecDatesExp.fromSession(request.session).get)
        val prices = pricesTaxesExp.fill(PricesTaxesExp.fromSession(request.session).get)
        Ok(export_check_details(journey,
                                traderCheck,
                                merchandise,
                                departure,
                                prices,
                                countriesService.getCountries,
                                ExportPages.check_details.toString, ExportPages.prices_taxes.toString))
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
      case Some(ExportPages.journey_details.case_value) => {
        journeyDetailsExp.bindFromRequest().fold(
          formWithErrors => Ok(export_journey_details(
            formWithErrors, countriesService.getCountries, ExportPages.journey_details.toString
          )),
          {
            valueInForm =>
              {
                Ok(export_trader_details(TraderDetailsExp.fromSession(request.session).fold(traderDetailsExp)(traderDetailsExp.fill(_)),
                                         countriesService.getCountries, ExportPages.trader_details.toString, ExportPages.journey_details.toString))
                  .addingToSession(JourneyDetailsExp.toSession(valueInForm): _*)
              }
          }
        )
      }
      //------------
      case Some(ExportPages.trader_details.case_value) => {
        traderDetailsExp.bindFromRequest().fold(
          formWithErrors => Ok(export_trader_details(
            formWithErrors, countriesService.getCountries, ExportPages.trader_details.toString, ExportPages.journey_details.toString
          )),
          {
            valueInForm =>
              {

                Ok(export_merchandise_details(
                  MerchandiseDetailsExp.fromSession(request.session).fold(merchandiseDetailsExp)(merchandiseDetailsExp.fill(_)),
                  ExportPages.merchandise_details.toString, ExportPages.trader_details.toString)).addingToSession(TraderDetailsExp.toSession(valueInForm): _*)
              }
          }
        )
      }
      //------------
      case Some(ExportPages.merchandise_details.case_value) => {
        merchandiseDetailsExp.bindFromRequest().fold(
          formWithErrors => Ok(export_merchandise_details(
            formWithErrors, ExportPages.merchandise_details.toString, ExportPages.trader_details.toString
          )),
          {
            valueInForm =>
              {
                Ok(export_departuredec_dates(DepartureDecDatesExp.fromSession(request.session).fold(departureDecDatesExp)(departureDecDatesExp.fill(_)),
                                             ExportPages.departuredec_dates.toString, ExportPages.merchandise_details.toString)).addingToSession(MerchandiseDetailsExp.toSession(valueInForm): _*)
              }
          }
        )
      }
      //------------
      case Some(ExportPages.departuredec_dates.case_value) => {
        departureDecDatesExp.bindFromRequest().fold(
          formWithErrors => Ok(export_departuredec_dates(
            formWithErrors,
            ExportPages.departuredec_dates.toString, ExportPages.merchandise_details.toString
          )),
          {
            valueInForm =>
              {
                Ok(export_prices_taxes(PricesTaxesExp.fromSession(request.session).fold(pricesTaxesExp)(pricesTaxesExp.fill(_)),
                                       ExportPages.prices_taxes.toString, ExportPages.departuredec_dates.toString)).addingToSession(DepartureDecDatesExp.toSession(valueInForm): _*)
              }
          }
        )
      }
      //------------
      case Some(ExportPages.prices_taxes.case_value) => {
        pricesTaxesExp.bindFromRequest().fold(
          formWithErrors => Ok(export_prices_taxes(
            formWithErrors, ExportPages.prices_taxes.toString, ExportPages.departuredec_dates.toString
          )),
          {
            valueInForm =>
              {
                val journey = journeyDetailsExp.fill(JourneyDetailsExp.fromSession(request.session).get)
                val traderFull = traderDetailsExp.fill(TraderDetailsExp.fromSession(request.session).get).get
                val merchandise = merchandiseDetailsExp.fill(MerchandiseDetailsExp.fromSession(request.session).get)
                val arrival = departureDecDatesExp.fill(DepartureDecDatesExp.fromSession(request.session).get)
                val traderCheck = traderDetailsCheckExp.fill(TraderDetailsCheckExp(traderFull.getFormattedAddress(countriesService.getCountry(traderFull.country)), traderFull.vrn, traderFull.vehicleRegNo))
                Ok(export_check_details(journey,
                                        traderCheck,
                                        merchandise,
                                        arrival,
                                        pricesTaxesExp.fill(valueInForm),
                                        countriesService.getCountries,
                                        ExportPages.check_details.toString, ExportPages.prices_taxes.toString)).addingToSession(PricesTaxesExp.toSession(valueInForm): _*)
              }
          }
        )
      }
      //------------
      case Some(ExportPages.check_details.case_value) => {
        val prices = PricesTaxesExp.fromSession(request.session).get
        Ok(export_declare_merchandise(declareExp, ExportPages.declare.toString, ExportPages.check_details.toString))
      }
      //------------
    }
  }

}
