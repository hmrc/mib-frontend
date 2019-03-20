package controllers.exp

import service.CountriesService
import config.AppConfig
import controllers.FormsConstraints
import controllers.FormsShared.{merchandiseDetails, traderDetails}
import exceptions.MibException
import javax.inject.{Inject, Singleton}
import model.shared.{MerchandiseDetails, TraderDetails}
import model.{ExportPages, MibTypes}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{AnyContent, Request, Results}
import views.html.shared.{merchandise_details, trader_details}

import scala.concurrent.ExecutionContext

@Singleton
class ExportTraderDetailsRequest @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService)(implicit ec: ExecutionContext, appConfig: AppConfig) extends I18nSupport with Results {

  def post(implicit request: Request[AnyContent]) = {
    traderDetails.bindFromRequest().fold(
      formWithErrors => Ok(trader_details(
        formWithErrors, countriesService.getCountries, ExportPages.trader_details.toString,
        controllers.routes.ExportController.getExportPage(ExportPages.journey_details.case_value), controllers.routes.ExportController.submitExportPage()
      )),
      {
        valueInForm =>
          {
            val traderValidate = FormsConstraints.validateTraderDetailsNoTrader(traderDetails.fill(valueInForm))
            val line1Validate = FormsConstraints.validateTraderDetailsNoLine1(traderValidate)
            val postcodeValidate = FormsConstraints.validateTraderDetailsNoPostCodeOrCountry(line1Validate)

            if (postcodeValidate.errors.size > 0) {
              Ok(trader_details(postcodeValidate,
                                countriesService.getCountries, ExportPages.trader_details.toString,
                                controllers.routes.ExportController.getExportPage(ExportPages.journey_details.case_value), controllers.routes.ExportController.submitExportPage()
              ))
            } else {
              Ok(merchandise_details(
                MerchandiseDetails.fromSession(request.session, MibTypes.mibExport).fold(merchandiseDetails(MibTypes.mibExport))(merchandiseDetails(MibTypes.mibExport).fill(_)),
                ExportPages.merchandise_details.toString,
                controllers.routes.ExportController.getExportPage(ExportPages.trader_details.case_value),
                controllers.routes.ExportController.submitExportPage(), "export.merchandisedetails.desciptionOfGoods"
              )).addingToSession(TraderDetails.toSession(valueInForm, MibTypes.mibExport): _*)
            }
          }
      }
    )

  }

  def get(implicit request: Request[AnyContent]) = {
    Ok(trader_details(traderDetails.fill(TraderDetails.fromSession(request.session, MibTypes.mibExport).getOrElse(throw new MibException("Trader Details not found"))),
                      countriesService.getCountries, ExportPages.trader_details.toString,
                      controllers.routes.ExportController.getExportPage(ExportPages.journey_details.case_value), controllers.routes.ExportController.submitExportPage()
    ))
  }
}
