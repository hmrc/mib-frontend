package controllers.imp

import config.AppConfig
import controllers.FormsShared.{importExportDate, prices}
import exceptions.MibException
import javax.inject.{Inject, Singleton}
import model.shared.{ImportExportDate, Prices}
import model.{ImportPages, MibTypes}
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{AnyContent, Request, Results}
import views.html.shared.{import_export_date, purchase_prices}

import scala.concurrent.ExecutionContext

@Singleton
class ImportPricesRequest @Inject() (val messagesApi: MessagesApi)(implicit ec: ExecutionContext, appConfig: AppConfig) extends I18nSupport with Results {

  def post(implicit request: Request[AnyContent]) = {
    prices.bindFromRequest().fold(
      formWithErrors => {
        Ok(purchase_prices(
          formWithErrors, prices.withError("", if (formWithErrors.errors.find(_.message == "error.real").isDefined) "error.max.purchase.blank" else "error.max.purchase.full"), ImportPages.prices.case_value,
          controllers.routes.ImportController.submitImportPage()
        ))
      },
      {
        valueInForm =>
          {
            Ok(import_export_date(ImportExportDate.fromSession(request.session, MibTypes.mibImport).fold(importExportDate)(importExportDate.fill(_)), importExportDate,
                                  ImportPages.import_date.case_value, controllers.routes.ImportController.getImportPage(ImportPages.prices.case_value),
                                  controllers.routes.ImportController.submitImportPage(),
              "import.date.header")).addingToSession(Prices.toSession(valueInForm, MibTypes.mibImport): _*)
          }
      }
    )
  }

  def get(implicit request: Request[AnyContent]) = {
    Ok(purchase_prices(prices.fill(Prices.fromSession(request.session, MibTypes.mibImport).getOrElse(throw new MibException("Prices Details not found"))), prices,
                       ImportPages.prices.case_value, controllers.routes.ImportController.submitImportPage()))
  }
}
