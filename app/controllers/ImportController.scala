package controllers

import Service.CountriesService
import config.AppConfig
import controllers.FormsImp._
import javax.inject.{Inject, Singleton}
import model.ImportPages
import model.imp.{TraderDetailsImp, _}
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.importpages._

import scala.concurrent.ExecutionContext

@Singleton
class ImportController @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService)
  (implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendController with I18nSupport {

  //------------------------------------------------------------------------------------------------------------------------------
  def getImportPage(page: String): Action[AnyContent] = Action { implicit request =>
    page match {
      case "journey_details" => Ok(journey_details(journeyDetailsImp.fill(JourneyDetailsImp.fromSession(request.session).get),
                                                   countriesService.getCountries, ImportPages.journey_details.case_value))
      case "trader_details" =>
        Ok(trader_details(traderDetailsImp.fill(TraderDetailsImp.fromSession(request.session).get), countriesService.getCountries,
                          ImportPages.trader_details.case_value, ImportPages.journey_details.case_value))

      case "merchandise_details" => Ok(merchandise_details(merchandiseDetailsImp.fill(MerchandiseDetailsImp.fromSession(request.session).get),
                                                           ImportPages.merchandise_details.case_value, ImportPages.trader_details.case_value))

      case "arrivaldec_dates" => Ok(arrivaldec_dates(arrivalDecDatesImp.fill(ArrivalDecDatesImp.fromSession(request.session).get),
                                                     ImportPages.arrivaldec_dates.case_value, ImportPages.merchandise_details.case_value))

      case "prices_taxes" => Ok(prices_taxes(pricesTaxesImp.fill(PricesTaxesImp.fromSession(request.session).get),
                                             ImportPages.prices_taxes.case_value, ImportPages.arrivaldec_dates.case_value))

      case "check_details" => {
        val journey = journeyDetailsImp.fill(JourneyDetailsImp.fromSession(request.session).get)
        val traderFull = traderDetailsImp.fill(TraderDetailsImp.fromSession(request.session).get).get
        val merchandise = merchandiseDetailsImp.fill(MerchandiseDetailsImp.fromSession(request.session).get)
        val traderCheck = traderDetailsCheckImp.fill(TraderDetailsCheckImp(traderFull.getFormattedAddress(countriesService.getCountry(traderFull.country)), traderFull.vrn, traderFull.vehicleRegNo))
        val arrival = arrivalDecDatesImp.fill(ArrivalDecDatesImp.fromSession(request.session).get)
        val prices = pricesTaxesImp.fill(PricesTaxesImp.fromSession(request.session).get)
        Ok(check_details(journey,
                         traderCheck,
                         merchandise,
                         arrival,
                         prices,
                         countriesService.getCountries,
                         ImportPages.check_details.case_value,
                         ImportPages.prices_taxes.case_value))
      }

      case "tax_due" => {
        val prices = PricesTaxesImp.fromSession(request.session).get
        val due = TaxDueImp(prices.purchasePrice, prices.customsDuty, prices.importVat, prices.customsDuty + prices.importVat)
        Ok(tax_due(taxDueImp.fill(due),
                   ImportPages.tax_due.case_value, ImportPages.check_details.case_value))
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
      case Some(ImportPages.journey_details.case_value) => {
        journeyDetailsImp.bindFromRequest().fold(
          formWithErrors => Ok(journey_details(
            formWithErrors, countriesService.getCountries,
            ImportPages.journey_details.case_value
          )),
          {
            valueInForm =>
              {
                Ok(trader_details(TraderDetailsImp.fromSession(request.session).fold(traderDetailsImp)(traderDetailsImp.fill(_)),
                                  countriesService.getCountries, ImportPages.trader_details.case_value, ImportPages.journey_details.case_value))
                  .addingToSession(JourneyDetailsImp.toSession(valueInForm): _*)
              }
          }
        )
      }
      //------------
      case Some(ImportPages.trader_details.case_value) => {
        traderDetailsImp.bindFromRequest().fold(
          formWithErrors => Ok(trader_details(
            formWithErrors, countriesService.getCountries,
            ImportPages.trader_details.case_value, ImportPages.journey_details.case_value
          )),
          {
            valueInForm =>
              {

                Ok(merchandise_details(
                  MerchandiseDetailsImp.fromSession(request.session).fold(merchandiseDetailsImp)(merchandiseDetailsImp.fill(_)),
                  ImportPages.merchandise_details.case_value, ImportPages.trader_details.case_value)).addingToSession(TraderDetailsImp.toSession(valueInForm): _*)
              }
          }
        )
      }
      //------------
      case Some(ImportPages.merchandise_details.case_value) => {
        merchandiseDetailsImp.bindFromRequest().fold(
          formWithErrors => Ok(merchandise_details(
            formWithErrors,
            ImportPages.merchandise_details.case_value,
            ImportPages.trader_details.case_value
          )),
          {
            valueInForm =>
              {
                Ok(arrivaldec_dates(ArrivalDecDatesImp.fromSession(request.session).fold(arrivalDecDatesImp)(arrivalDecDatesImp.fill(_)),
                                    ImportPages.arrivaldec_dates.case_value, ImportPages.merchandise_details.case_value)).addingToSession(MerchandiseDetailsImp.toSession(valueInForm): _*)
              }
          }
        )
      }
      //------------
      case Some(ImportPages.arrivaldec_dates.case_value) => {
        arrivalDecDatesImp.bindFromRequest().fold(
          formWithErrors => Ok(arrivaldec_dates(
            formWithErrors,
            ImportPages.arrivaldec_dates.case_value,
            ImportPages.merchandise_details.case_value
          )),
          {
            valueInForm =>
              {
                Ok(prices_taxes(PricesTaxesImp.fromSession(request.session).fold(pricesTaxesImp)(pricesTaxesImp.fill(_)),
                                ImportPages.prices_taxes.case_value, ImportPages.arrivaldec_dates.case_value)).addingToSession(ArrivalDecDatesImp.toSession(valueInForm): _*)
              }
          }
        )
      }
      //------------
      case Some(ImportPages.prices_taxes.case_value) => {
        pricesTaxesImp.bindFromRequest().fold(
          formWithErrors => Ok(prices_taxes(
            formWithErrors,
            ImportPages.prices_taxes.case_value,
            ImportPages.arrivaldec_dates.case_value
          )),
          {
            valueInForm =>
              {
                val journey = journeyDetailsImp.fill(JourneyDetailsImp.fromSession(request.session).get)
                val traderFull = traderDetailsImp.fill(TraderDetailsImp.fromSession(request.session).get).get
                val merchandise = merchandiseDetailsImp.fill(MerchandiseDetailsImp.fromSession(request.session).get)
                val arrival = arrivalDecDatesImp.fill(ArrivalDecDatesImp.fromSession(request.session).get)
                val traderCheck = traderDetailsCheckImp.fill(TraderDetailsCheckImp(traderFull.getFormattedAddress(countriesService.getCountry(traderFull.country)), traderFull.vrn, traderFull.vehicleRegNo))
                Ok(check_details(journey,
                                 traderCheck,
                                 merchandise,
                                 arrival,
                                 pricesTaxesImp.fill(valueInForm),
                                 countriesService.getCountries,
                                 ImportPages.check_details.case_value, ImportPages.prices_taxes.case_value)).addingToSession(PricesTaxesImp.toSession(valueInForm): _*)
              }
          }
        )
      }
      //------------
      case Some(ImportPages.check_details.case_value) => {
        val prices = PricesTaxesImp.fromSession(request.session).get
        val due = TaxDueImp(prices.purchasePrice, prices.customsDuty, prices.importVat, prices.customsDuty + prices.importVat)
        Ok(tax_due(taxDueImp.fill(due), ImportPages.tax_due.case_value, ImportPages.check_details.case_value))
      }
      //------------
      case Some(ImportPages.tax_due.case_value) => {
        Ok(declare_merchandise(declareImp, ImportPages.declare.case_value, ImportPages.tax_due.case_value))
      }
      //------------
    }
  }

}
