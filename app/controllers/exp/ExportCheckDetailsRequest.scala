package controllers.exp

import java.text.SimpleDateFormat
import java.util.Calendar

import Service.{CountriesService, RefService}
import config.AppConfig
import controllers.FormsExp._
import controllers.FormsShared._
import exceptions.MibException
import javax.inject.{Inject, Singleton}
import model.exp.{DeclarationReceived, JourneyDetailsExp, TraderDetailsCheckExp}
import model.shared.{ImportExportDate, MerchandiseDetails, Prices, TraderDetails}
import model.{ExportPages, MibTypes}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{AnyContent, Request, Results}
import views.html.exportpages.{declaration_received, export_check_details}

import scala.concurrent.ExecutionContext

@Singleton
class ExportCheckDetailsRequest @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService, refService: RefService)(implicit ec: ExecutionContext, appConfig: AppConfig) extends I18nSupport with Results {

  def post(implicit request: Request[AnyContent]) = {
    val traderFull = traderDetails.fill(TraderDetails.fromSession(request.session, MibTypes.mibExport)
      .getOrElse(throw new MibException("Trader Details not found"))).get
    val merchDetails = MerchandiseDetails.fromSession(request.session, MibTypes.mibExport).getOrElse(throw new MibException("Merchant details not found"))
    val dateTime = Calendar.getInstance.getTime
    val dateFormat = new SimpleDateFormat("dd LLLL yyyy")
    val decRecd = DeclarationReceived(dateFormat.format(dateTime),
                                      traderFull.getFormattedAddress(traderFull.country.fold("")(countriesService.getCountry(_))), merchDetails.desciptionOfGoods, refService.exportRef)
    Ok(declaration_received(declarationReceived.fill(decRecd),
                            ExportPages.dec_received.case_value, ExportPages.check_details.case_value)).addingToSession(DeclarationReceived.toSession(decRecd): _*)
  }

  def get(implicit request: Request[AnyContent]) = {
    val journey = journeyDetailsExp.fill(JourneyDetailsExp.fromSession(request.session).getOrElse(throw new MibException("Journey Details not found")))
    val traderFull = traderDetails.fill(TraderDetails.fromSession(request.session, MibTypes.mibExport).getOrElse(throw new MibException("Trader Details not found"))).get
    val merchandise = merchandiseDetails.fill(MerchandiseDetails.fromSession(request.session, MibTypes.mibExport).getOrElse(throw new MibException("Merchant Details not found")))
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
