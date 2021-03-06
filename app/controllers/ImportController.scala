package controllers

import audit._
import audit.imp.{ImportDeclarationCreateAudit, PricesTaxesAudit}
import config.AppConfig
import connector.{MibBackendConnector, PayApiConnector}
import controllers.FormsShared.traderDetails
import controllers.imp._
import exceptions.MibException
import javax.inject.{Inject, Singleton}
import model.imp.{JourneyDetailsImp, PricesTaxesImp}

import model.payapi.SpjRequest
import model.shared.{ImportExportDate, MerchandiseDetails, Prices, TraderDetails}
import model.{ImportPages, MibTypes, YesNoValues}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import service.{CountriesService, RefService}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.error_template

import scala.concurrent.ExecutionContext

@Singleton
class ImportController @Inject() (countriesService: CountriesService,
                                  pricesRequest:    ImportPricesRequest, importDateRequest: ImportDateRequest,
                                  pricesTaxesRequest: PricesTaxesRequest, importJourneyDetailsRequest: ImportJourneyDetailsRequest,
                                  importTraderDetailsRequest: ImportTraderDetailsRequest, importMerchandiseDetails: ImportMerchandiseDetails,
                                  taxDueRequest: TaxDueRequest, importCheckDetailsRequest: ImportCheckDetailsRequest, payApiConnector: PayApiConnector,
                                  refService: RefService, auditor: Auditor, mibBackendConnector: MibBackendConnector, mcc: MessagesControllerComponents)
  (implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendController(mcc) {

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
        BadRequest(error_template("Merchandise in Baggage", "Merchandise in Baggage", "Page not found"))
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
    val traderDetail = TraderDetails.fromSession(request.session, MibTypes.mibImport).getOrElse(throw new MibException("Trader Details not found"))
    val traderFull = traderDetails.fill(traderDetail).get
    val address = traderFull.getFormattedAddress(traderFull.country.fold("")(countriesService.getCountry(_)))
    val pTax = PricesTaxesImp.fromSession(request.session).getOrElse(throw new MibException("PricesTaxesImp details not found"))
    val customsDutyPence = (pTax.customsDuty * 100).intValue()
    val importVatPence = (pTax.importVat * 100).intValue()
    val amtInPence = customsDutyPence + importVatPence

    val description = MerchandiseDetails.fromSession(request.session, MibTypes.mibImport).getOrElse(throw new MibException("Merchandise Details not found")).desciptionOfGoods

    //Auditor
    val journey = JourneyDetailsImp.fromSession(request.session).getOrElse(throw new MibException("Journey Details not found"))
    val journeyWithCountryFull = journey.copy(countryOfOrigin = countriesService.getCountry(journey.countryOfOrigin))
    val pricesVal = Prices.fromSession(request.session, MibTypes.mibImport).getOrElse(throw new MibException("Prices Details not found"))
    val departure = ImportExportDate.fromSession(request.session, MibTypes.mibImport).getOrElse(throw new MibException("ImportExport details not found"))
    val merchDetails = MerchandiseDetails.fromSession(request.session, MibTypes.mibImport).getOrElse(throw new MibException("Merchant details not found"))

    val customs: Int = pTax.customsDuty.toInt * 100
    val importDuty: Int = pTax.importVat.toInt * 100
    val priceTaxesAudit: PricesTaxesAudit = new PricesTaxesAudit(customs, importDuty, customs + importDuty)

    val declarationCreate: ImportDeclarationCreateAudit = ImportDeclarationCreateAudit(purchasePriceInPence = pricesVal.purchasePrice.toInt * 100, importDate = departure.stringValue)

    val traderDetailsForAudit = traderDetail.uk match {
      case YesNoValues.yes => {
        TraderDetailsForAudit(NameOfTrader(traderDetail.trader),
                              Option(traderDetail.getAddressObjectUk()), None,
                              traderDetail.vrn, traderDetail.vehicleRegNo)
      }
      case YesNoValues.no => {
        TraderDetailsForAudit(NameOfTrader(traderDetail.trader),
                              None, Option(traderDetail.getAddressObjectNonUk(traderDetail.country.fold("")(countriesService.getCountry(_)))),
                              traderDetail.vrn, traderDetail.vehicleRegNo)
      }
    }

    val mibRefernce = refService.importRef
    val auditData: ImportAuditData = ImportAuditData(submissionRef = SubmissionRef(mibRefernce), declarationCreate, priceTaxesAudit, journeyWithCountryFull, merchDetails, traderDetailsForAudit)

    auditor.sendAuditData(auditData, MibTypes.mibImport, "merchandiseDeclaration")
    //End Audit

    val spjRequest = SpjRequest(mibReference       = mibRefernce,
                                vatAmountInPence   = importVatPence,
                                dutyAmountInPence  = customsDutyPence,
                                amountInPence      = amtInPence,
                                traderDetails      = address,
                                merchandiseDetails = description)

    for {
      storeImport <- mibBackendConnector.storeImport(auditData)
      response <- payApiConnector.createJourney(spjRequest)
    } yield Redirect(response.nextUrl)
  }
}
