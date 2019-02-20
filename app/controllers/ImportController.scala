package controllers

import Service.CountriesService
import config.AppConfig
import controllers.Forms.{merchandiseDetails, _}
import javax.inject.{Inject, Singleton}
import model._
import play.api.Logger
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Request}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.importpages._

import scala.concurrent.ExecutionContext

@Singleton
class ImportController @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService)(implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendController with I18nSupport {

  //------------------------------------------------------------------------------------------------------------------------------
  def getImportPage(page: String): Action[AnyContent] = Action { implicit request =>
    page match {
      case "journey_details" => Ok(journey_details(journeyDetails.fill(JourneyDetails.fromSession(request.session).get),
                                                   countriesService.getCountries))
      case "trader_details" =>
        Ok(trader_details(traderDetails.fill(TraderDetails.fromSession(request.session).get), countriesService.getCountries))
      case "merchandise_details" => Ok(merchandise_details(merchandiseDetails.fill(MerchandiseDetails.fromSession(request.session).get)))
      case "arrivaldec_dates"    => Ok(arrivaldec_dates(arrivalDecDates.fill(ArrivalDecDates.fromSession(request.session).get)))
      case "prices_taxes"        => Ok(prices_taxes(pricesTaxes.fill(PricesTaxes.fromSession(request.session).get)))
      case "check_details" => {
        val journey = journeyDetails.fill(JourneyDetails.fromSession(request.session).get)
        val traderFull = traderDetails.fill(TraderDetails.fromSession(request.session).get).get
        val merchandise = merchandiseDetails.fill(MerchandiseDetails.fromSession(request.session).get)
        val traderCheck = traderDetailsCheck.fill(TraderDetailsCheck(traderFull.getFormattedAddress(countriesService.getCountry(traderFull.country)), traderFull.vrn, traderFull.vehicleRegNo))
        val arrival = arrivalDecDates.fill(ArrivalDecDates.fromSession(request.session).get)
        val prices = pricesTaxes.fill(PricesTaxes.fromSession(request.session).get)
        Ok(check_details(journey,
                         traderCheck,
                         merchandise,
                         arrival,
                         prices,
                         countriesService.getCountries))
      }
      case "tax_due" => {
        val prices = PricesTaxes.fromSession(request.session).get
        val due = TaxDue("tax_due", prices.purchasePrice, prices.customsDuty, prices.importVat, prices.customsDuty + prices.importVat)
        Ok(tax_due(taxDue.fill(due)))
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
      case Some("journey_details") => {
        Forms.journeyDetails.bindFromRequest().fold(
          formWithErrors => Ok(journey_details(
            formWithErrors, countriesService.getCountries
          )),
          {
            valueInForm =>
              {
                Ok(trader_details(TraderDetails.fromSession(request.session).fold(traderDetails)(traderDetails.fill(_)), countriesService.getCountries))
                  .addingToSession(JourneyDetails.toSession(valueInForm): _*)
              }
          }
        )
      }
      //------------
      case Some("trader_details") => {
        Forms.traderDetails.bindFromRequest().fold(
          formWithErrors => Ok(trader_details(
            formWithErrors, countriesService.getCountries
          )),
          {
            valueInForm =>
              {

                Ok(merchandise_details(
                  MerchandiseDetails.fromSession(request.session).fold(merchandiseDetails)(merchandiseDetails.fill(_)))).addingToSession(TraderDetails.toSession(valueInForm): _*)
              }
          }
        )
      }
      //------------
      case Some("merchandise_details") => {
        Forms.merchandiseDetails.bindFromRequest().fold(
          formWithErrors => Ok(merchandise_details(
            formWithErrors
          )),
          {
            valueInForm =>
              {
                Ok(arrivaldec_dates(ArrivalDecDates.fromSession(request.session).fold(arrivalDecDates)(arrivalDecDates.fill(_)))).addingToSession(MerchandiseDetails.toSession(valueInForm): _*)
              }
          }
        )
      }
      //------------
      case Some("arrivaldec_dates") => {
        Forms.arrivalDecDates.bindFromRequest().fold(
          formWithErrors => Ok(arrivaldec_dates(
            formWithErrors
          )),
          {
            valueInForm =>
              {
                Ok(prices_taxes(PricesTaxes.fromSession(request.session).fold(pricesTaxes)(pricesTaxes.fill(_)))).addingToSession(ArrivalDecDates.toSession(valueInForm): _*)
              }
          }
        )
      }
      //------------
      case Some("prices_taxes") => {
        Forms.pricesTaxes.bindFromRequest().fold(
          formWithErrors => Ok(prices_taxes(
            formWithErrors
          )),
          {
            valueInForm =>
              {
                val journey = journeyDetails.fill(JourneyDetails.fromSession(request.session).get)
                val traderFull = traderDetails.fill(TraderDetails.fromSession(request.session).get).get
                val merchandise = merchandiseDetails.fill(MerchandiseDetails.fromSession(request.session).get)
                val arrival = arrivalDecDates.fill(ArrivalDecDates.fromSession(request.session).get)
                val traderCheck = traderDetailsCheck.fill(TraderDetailsCheck(traderFull.getFormattedAddress(countriesService.getCountry(traderFull.country)), traderFull.vrn, traderFull.vehicleRegNo))
                Ok(check_details(journey,
                                 traderCheck,
                                 merchandise,
                                 arrival,
                                 pricesTaxes.fill(valueInForm),
                                 countriesService.getCountries)).addingToSession(PricesTaxes.toSession(valueInForm): _*)
              }
          }
        )
      }
      //------------
      case Some("check_details") => {
        val prices = PricesTaxes.fromSession(request.session).get
        val due = TaxDue("tax_due", prices.purchasePrice, prices.customsDuty, prices.importVat, prices.customsDuty + prices.importVat)
        Ok(tax_due(taxDue.fill(due)))
      }
      //------------
      case Some("tax_due") => {
        Ok(declare_merchandise(declare))
      }
      //------------
    }
  }

}
