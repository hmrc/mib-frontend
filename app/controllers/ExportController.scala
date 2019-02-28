package controllers

import Service.CountriesService
import config.AppConfig
import controllers.FormsExp._
import javax.inject.{Inject, Singleton}
import model.{ExportPages, MibTypes}
import model.exp._
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.exportpages._
import controllers.FormsShared._
import model.shared._
import views.html.shared.purchase_prices
import views.html.shared._

import scala.concurrent.ExecutionContext

@Singleton
class ExportController @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService)
  (implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendController with I18nSupport {

  //------------------------------------------------------------------------------------------------------------------------------
  def getExportPage(page: String): Action[AnyContent] = Action { implicit request =>

    page match {

      case ExportPages.prices.case_value =>
        Ok(purchase_prices(prices.fill(Prices.fromSession(request.session, MibTypes.mibExport).get),
                           ExportPages.prices.toString, controllers.routes.ExportController.submitExportPage()))

      case ExportPages.export_date.case_value =>
        Ok(import_export_date(importExportDate.fill(ImportExportDate.fromSession(request.session, MibTypes.mibExport).get),
                              ExportPages.export_date.toString, controllers.routes.ExportController.getExportPage(ExportPages.prices.case_value),
                              controllers.routes.ExportController.submitExportPage(), "export.date.header"))

      case ExportPages.journey_details.case_value => Ok(export_journey_details(journeyDetailsExp.fill(JourneyDetailsExp.fromSession(request.session).get),
                                                                               countriesService.getCountries, ExportPages.journey_details.toString, ExportPages.export_date.case_value))

      case ExportPages.trader_details.case_value =>
        Ok(trader_details(traderDetails.fill(TraderDetails.fromSession(request.session, MibTypes.mibExport).get),
                          countriesService.getCountries, ExportPages.trader_details.toString,
                          controllers.routes.ExportController.getExportPage(ExportPages.journey_details.case_value), controllers.routes.ExportController.submitExportPage()
        ))

      case ExportPages.merchandise_details.case_value =>
        Ok(merchandise_details(merchandiseDetails.fill(MerchandiseDetails.fromSession(request.session, MibTypes.mibExport).get),
                               ExportPages.merchandise_details.toString,
                               controllers.routes.ExportController.getExportPage(ExportPages.trader_details.case_value),
                               controllers.routes.ExportController.submitExportPage(), "export.merchandisedetails.desciptionOfGoods"))

      case ExportPages.check_details.case_value => {
        val journey = journeyDetailsExp.fill(JourneyDetailsExp.fromSession(request.session).get)
        val traderFull = traderDetails.fill(TraderDetails.fromSession(request.session, MibTypes.mibExport).get).get
        val merchandise = merchandiseDetails.fill(MerchandiseDetails.fromSession(request.session, MibTypes.mibExport).get)
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
      //------------
      case Some(ExportPages.prices.case_value) => {
        prices.bindFromRequest().fold(
          formWithErrors => Ok(purchase_prices(
            formWithErrors, ExportPages.prices.case_value,
            controllers.routes.ExportController.submitExportPage()
          )),
          {
            valueInForm =>
              {
                Ok(import_export_date(ImportExportDate.fromSession(request.session, MibTypes.mibExport).fold(importExportDate)(importExportDate.fill(_)),
                                      ExportPages.export_date.case_value, controllers.routes.ExportController.getExportPage(ExportPages.prices.case_value),
                                      controllers.routes.ExportController.submitExportPage(), "export.date.header")).addingToSession(Prices.toSession(valueInForm, MibTypes.mibExport): _*)
              }
          }
        )
      }

      //------------
      case Some(ExportPages.export_date.case_value) => {
        importExportDate.bindFromRequest().fold(
          formWithErrors => Ok(import_export_date(
            formWithErrors,
            ExportPages.export_date.toString, controllers.routes.ExportController.getExportPage(ExportPages.prices.case_value),
            controllers.routes.ExportController.submitExportPage(), "export.date.header"
          )),
          {
            valueInForm =>
              {
                Ok(export_journey_details(JourneyDetailsExp.fromSession(request.session).fold(journeyDetailsExp)(journeyDetailsExp.fill(_)), countriesService.getCountries,
                                          ExportPages.journey_details.case_value, ExportPages.export_date.case_value)).addingToSession(ImportExportDate.toSession(valueInForm, MibTypes.mibExport): _*)

              }
          }
        )
      }
      //------------

      case Some(ExportPages.journey_details.case_value) => {
        journeyDetailsExp.bindFromRequest().fold(
          formWithErrors => Ok(export_journey_details(
            formWithErrors, countriesService.getCountries, ExportPages.journey_details.toString, ExportPages.export_date.case_value
          )),
          {
            valueInForm =>
              {
                Ok(trader_details(TraderDetails.fromSession(request.session, MibTypes.mibExport).fold(traderDetails)(traderDetails.fill(_)),
                                  countriesService.getCountries, ExportPages.trader_details.toString,
                                  controllers.routes.ExportController.getExportPage(ExportPages.journey_details.case_value), controllers.routes.ExportController.submitExportPage()
                ))
                  .addingToSession(JourneyDetailsExp.toSession(valueInForm): _*)
              }
          }
        )
      }
      //------------
      case Some(ExportPages.trader_details.case_value) => {
        traderDetails.bindFromRequest().fold(
          formWithErrors => Ok(trader_details(
            formWithErrors, countriesService.getCountries, ExportPages.trader_details.toString,
            controllers.routes.ExportController.getExportPage(ExportPages.journey_details.case_value), controllers.routes.ExportController.submitExportPage()
          )),
          {
            valueInForm =>
              {
                val postcodeValidate = FormsConstraints.validateTraderDetailsNoPostCodeOrCountry(traderDetails.fill(valueInForm))
                val line1Validate = FormsConstraints.validateTraderDetailsNoLine1(postcodeValidate)

                if (line1Validate.errors.size > 0) {
                  Ok(trader_details(line1Validate,
                                    countriesService.getCountries, ExportPages.trader_details.toString,
                                    controllers.routes.ExportController.getExportPage(ExportPages.journey_details.case_value), controllers.routes.ExportController.submitExportPage()
                  ))
                } else {
                  Ok(merchandise_details(
                    MerchandiseDetails.fromSession(request.session, MibTypes.mibExport).fold(merchandiseDetails)(merchandiseDetails.fill(_)),
                    ExportPages.merchandise_details.toString,
                    controllers.routes.ExportController.getExportPage(ExportPages.trader_details.case_value),
                    controllers.routes.ExportController.submitExportPage(), "export.merchandisedetails.desciptionOfGoods"
                  )).addingToSession(TraderDetails.toSession(valueInForm, MibTypes.mibExport): _*)
                }
              }
          }
        )
      }
      //------------
      case Some(ExportPages.merchandise_details.case_value) => {
        merchandiseDetails.bindFromRequest().fold(
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
                val merchandise = merchandiseDetails.fill(valueInForm)
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

      //------------
      case Some(ExportPages.check_details.case_value) => {
        Ok
      }
      //------------
    }
  }

}
