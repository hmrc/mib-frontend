package controllers.exp

import service.CountriesService
import config.AppConfig
import controllers.FormsExp._
import controllers.FormsShared._
import exceptions.MibException
import javax.inject.{Inject, Singleton}
import model.exp.{JourneyDetailsExp, TraderDetailsCheckExp}
import model.shared.{ImportExportDate, MerchandiseDetails, Prices, TraderDetails}
import model.{ExportPages, MibTypes}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{AnyContent, Request, Results}
import views.html.exportpages.export_check_details
import views.html.shared.merchandise_details

import scala.concurrent.ExecutionContext

@Singleton
class ExportMerchandiseDetailsRequest @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService)(implicit ec: ExecutionContext, appConfig: AppConfig) extends I18nSupport with Results {

  def post(implicit request: Request[AnyContent]) = {
    merchandiseDetails(MibTypes.mibExport).bindFromRequest().fold(
      formWithErrors => Ok(merchandise_details(
        formWithErrors, ExportPages.merchandise_details.toString,
        controllers.routes.ExportController.getExportPage(ExportPages.trader_details.case_value),
        controllers.routes.ExportController.submitExportPage(), "export.merchandisedetails.desciptionOfGoods"
      )),
      {
        valueInForm =>
          {
            val journey = journeyDetailsExp.fill(JourneyDetailsExp.fromSession(request.session).get)
            val traderFull = traderDetails.fill(TraderDetails.fromSession(request.session, MibTypes.mibExport).get).get
            val merchandise = merchandiseDetails(MibTypes.mibExport).fill(valueInForm)
            val traderCheck = traderDetailsCheckExp.fill(TraderDetailsCheckExp(traderFull.getFormattedAddress(traderFull.country.fold("")(countriesService.getCountry(_))), traderFull.vrn, traderFull.vehicleRegNo))
            val departure = importExportDate.fill(ImportExportDate.fromSession(request.session, MibTypes.mibExport).get)
            val pricesVal = prices.fill(Prices.fromSession(request.session, MibTypes.mibExport).get)
            Ok(export_check_details(journey,
                                    traderCheck,
                                    merchandise,
                                    departure,
                                    pricesVal,
                                    countriesService.getCountries,
                                    ExportPages.check_details.toString, ExportPages.merchandise_details.toString))
              .addingToSession(MerchandiseDetails.toSession(valueInForm, MibTypes.mibExport): _*)
          }
      }
    )
  }

  def get(implicit request: Request[AnyContent]) = {
    Ok(merchandise_details(merchandiseDetails(MibTypes.mibExport).fill(MerchandiseDetails.fromSession(request.session, MibTypes.mibExport).getOrElse(throw new MibException("Merchandise Details not found"))),
                           ExportPages.merchandise_details.toString,
                           controllers.routes.ExportController.getExportPage(ExportPages.trader_details.case_value),
                           controllers.routes.ExportController.submitExportPage(), "export.merchandisedetails.desciptionOfGoods"))
  }
}
