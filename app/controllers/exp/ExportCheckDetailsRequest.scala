package controllers.exp

import java.text.SimpleDateFormat
import java.util.Calendar

import audit._
import audit.exp.ExportDeclarationCreateAudit
import config.AppConfig
import connector.MibBackendConnector
import controllers.FormsExp._
import controllers.FormsShared._
import exceptions.MibException
import javax.inject.{Inject, Singleton}
import model.exp.{DeclarationReceived, JourneyDetailsExp, TraderDetailsCheckExp}
import model.mib.StoreResponse
import model.shared._
import model.{ExportPages, MibTypes, YesNoValues}
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{AnyContent, Request, Results}
import service.{CountriesService, RefService}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.HeaderCarrierConverter
import views.html.exportpages.{declaration_received, export_check_details}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ExportCheckDetailsRequest @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService, refService: RefService,
                                           auditor: Auditor, mibBackendConnector: MibBackendConnector)
  (implicit ec: ExecutionContext, appConfig: AppConfig) extends I18nSupport with Results {

  def post(implicit request: Request[AnyContent]) = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))

    val traderDetail = TraderDetails.fromSession(request.session, MibTypes.mibExport).getOrElse(throw new MibException("Trader Details not found"))
    val merchDetails = MerchandiseDetails.fromSession(request.session, MibTypes.mibExport).getOrElse(throw new MibException("Merchant details not found"))
    val pricesVal = Prices.fromSession(request.session, MibTypes.mibExport).getOrElse(throw new MibException("Prices Details not found"))
    val departure = ImportExportDate.fromSession(request.session, MibTypes.mibExport).getOrElse(throw new MibException("ImportExport details not found"))
    val journey = JourneyDetailsExp.fromSession(request.session).getOrElse(throw new MibException("Journey Details not found"))

    val dateFormat = new SimpleDateFormat("dd LLLL yyyy")
    val dateTime = Calendar.getInstance.getTime

    val decRecd = DeclarationReceived(dateFormat.format(dateTime),
                                      traderDetail.getFormattedAddress(traderDetail.country.fold("")(countriesService.getCountry(_))), merchDetails.desciptionOfGoods, refService.exportRef)

    //Auditor
    val journeyWithCountryFull = journey.copy(destinationCountry = countriesService.getCountry(journey.destinationCountry))
    val declarationCreate: ExportDeclarationCreateAudit = ExportDeclarationCreateAudit(purchasePriceInPence = pricesVal.purchasePrice.toInt * 100, exportDate = departure.stringValue)

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

    val auditData: ExportAuditData = ExportAuditData(submissionRef = SubmissionRef(decRecd.mibReference), declarationCreate, journeyWithCountryFull, merchDetails, traderDetailsForAudit)

    auditor(auditData, MibTypes.mibExport, "merchandiseDeclaration")

    val response: Future[StoreResponse] = mibBackendConnector.storeExport(auditData)
    response.map(res => Logger.debug(res.value))

    Ok(declaration_received(declarationReceived.fill(decRecd),
                            ExportPages.dec_received.case_value, ExportPages.check_details.case_value)).addingToSession(DeclarationReceived.toSession(decRecd): _*)
  }

  def get(implicit request: Request[AnyContent]) = {
    val journey = journeyDetailsExp.fill(JourneyDetailsExp.fromSession(request.session).getOrElse(throw new MibException("Journey Details not found")))
    val traderFull = traderDetails.fill(TraderDetails.fromSession(request.session, MibTypes.mibExport).getOrElse(throw new MibException("Trader Details not found"))).get
    val merchandise = merchandiseDetails(MibTypes.mibExport).fill(MerchandiseDetails.fromSession(request.session, MibTypes.mibExport).getOrElse(throw new MibException("Merchant Details not found")))
    val traderCheck = traderDetailsCheckExp.fill(TraderDetailsCheckExp(traderFull.getFormattedAddress(traderFull.country.fold("")(countriesService.getCountry(_))), traderFull.vrn, traderFull.vehicleRegNo))
    val departure = importExportDate.fill(ImportExportDate.fromSession(request.session, MibTypes.mibExport).getOrElse(throw new MibException("ImportExport details not found")))
    val pricesVal = prices.fill(Prices.fromSession(request.session, MibTypes.mibExport).getOrElse(throw new MibException("Prices Details not found")))
    Ok(export_check_details(journey,
                            traderCheck,
                            merchandise,
                            departure,
                            pricesVal,
                            countriesService.getCountries,
                            ExportPages.check_details.toString, ExportPages.merchandise_details.toString))
  }
}
