package controllers

import Service.{CountriesService, RefService}
import config.AppConfig
import connector.PayApiConnector
import controllers.FormsShared.traderDetails
import controllers.imp._
import exceptions.MibException
import javax.inject.{Inject, Singleton}
import model.imp.PricesTaxesImp
import model.payapi.SpjRequest
import model.shared.{MerchandiseDetails, Prices, TraderDetails}
import model.{ImportPages, MibTypes}
import play.api.Logger
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
                                  taxDueRequest: TaxDueRequest, importCheckDetailsRequest: ImportCheckDetailsRequest, payApiConnector: PayApiConnector,
                                  refService: RefService)
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

      case _ => {
        Ok(error_template("Merchandise in Baggage", "Merchandise in Baggage", "Page not found"))
      }
    }
  }

  def startJourney: Action[AnyContent] = Action.async { implicit request =>
    val traderFull = traderDetails.fill(TraderDetails.fromSession(request.session, MibTypes.mibImport).getOrElse(throw new MibException("Trader Details not found"))).get
    val address = traderFull.getFormattedAddress(traderFull.country.fold("")(countriesService.getCountry(_)))
    val mibRefernce = refService.importRef
    val pTax = PricesTaxesImp.fromSession(request.session).getOrElse(throw new MibException("PricesTaxesImp details not found"))
    val amtInPence = (pTax.customsDuty + pTax.importVat) * 100

    val description = MerchandiseDetails.fromSession(request.session, MibTypes.mibImport).getOrElse(throw new MibException("Merchandise Details not found")).desciptionOfGoods

    val journeyRequest = SpjRequest(mibRef                 = mibRefernce,
                                    amountInPence          = amtInPence.intValue(), traderAddress = address, descriptionMerchandise = description)

    payApiConnector.createJourney(journeyRequest).map(response => {
      Logger.debug("redirecting to " + response.nextUrl)
      Redirect(response.nextUrl)
    })
  }

}
