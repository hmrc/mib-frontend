package controllers.imp

import service.CountriesService
import config.AppConfig
import controllers.FormsConstraints
import controllers.FormsShared.{merchandiseDetails, traderDetails}
import exceptions.MibException
import javax.inject.{Inject, Singleton}
import model.shared.{MerchandiseDetails, TraderDetails}
import model.{ImportPages, MibTypes}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{AnyContent, Request, Results}
import views.html.shared.{merchandise_details, trader_details}

import scala.concurrent.ExecutionContext

@Singleton
class ImportTraderDetailsRequest @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService)(implicit ec: ExecutionContext, appConfig: AppConfig) extends I18nSupport with Results {

  def post(implicit request: Request[AnyContent]) = {

    traderDetails.bindFromRequest().fold(
      formWithErrors => Ok(trader_details(
        formWithErrors, countriesService.getCountries,
        ImportPages.trader_details.case_value, controllers.routes.ImportController.getImportPage(ImportPages.journey_details.case_value), controllers.routes.ImportController.submitImportPage()
      )),
      {
        valueInForm =>
          {
            val traderValidate = FormsConstraints.validateTraderDetailsNoTrader(traderDetails.fill(valueInForm))
            val line1Validate = FormsConstraints.validateTraderDetailsNoLine1(traderValidate)
            val postcodeValidate = FormsConstraints.validateTraderDetailsNoPostCodeOrCountry(line1Validate)

            if (postcodeValidate.errors.size > 0) {
              Ok(trader_details(postcodeValidate,
                                countriesService.getCountries, ImportPages.trader_details.toString,
                                controllers.routes.ExportController.getExportPage(ImportPages.journey_details.case_value), controllers.routes.ExportController.submitExportPage()
              ))
            } else {
              Ok(merchandise_details(
                MerchandiseDetails.fromSession(request.session, MibTypes.mibImport).fold(merchandiseDetails(MibTypes.mibImport))(merchandiseDetails(MibTypes.mibImport).fill(_)),
                ImportPages.merchandise_details.case_value,
                controllers.routes.ImportController.getImportPage(ImportPages.trader_details.case_value),
                controllers.routes.ImportController.submitImportPage(), "import.merchandisedetails.desciptionOfGoods"
              )).addingToSession(TraderDetails.toSession(valueInForm, MibTypes.mibImport): _*)
            }
          }
      }
    )
  }

  def get(implicit request: Request[AnyContent]) = {
    Ok(trader_details(traderDetails.fill(TraderDetails.fromSession(request.session, MibTypes.mibImport).getOrElse(throw new MibException("Trader Details not found"))), countriesService.getCountries,
                      ImportPages.trader_details.case_value,
                      controllers.routes.ImportController.getImportPage(ImportPages.journey_details.case_value), controllers.routes.ImportController.submitImportPage()))

  }
}
