package controllers.exp

import config.AppConfig
import controllers.FormsShared.{importExportDate, prices}
import exceptions.MibException
import javax.inject.{Inject, Singleton}
import model.shared.{ImportExportDate, Prices}
import model.{ExportPages, MibTypes}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{AnyContent, Request, Results}
import views.html.shared.{import_export_date, purchase_prices}

import scala.concurrent.ExecutionContext

@Singleton
class ExportPricesRequest @Inject() (val messagesApi: MessagesApi)(implicit ec: ExecutionContext, appConfig: AppConfig) extends I18nSupport with Results {

  def post(implicit request: Request[AnyContent]) = {
    prices.bindFromRequest().fold(
      formWithErrors => {

        val newFormWithErrors = (if (formWithErrors.errors.find(_.message == "error.real").isDefined)
          formWithErrors.discardingErrors.withError("purchasePrice", "error.max.purchase.value")
        else
          formWithErrors)

        Ok(purchase_prices(
          newFormWithErrors, prices.withError("", "error.max.purchase.full"), ExportPages.prices.case_value,
          controllers.routes.ExportController.submitExportPage()
        ))
      },
      {
        valueInForm =>
          {
            Ok(import_export_date(ImportExportDate.fromSession(request.session, MibTypes.mibExport).fold(importExportDate)(importExportDate.fill(_)), importExportDate,
                                  ExportPages.export_date.case_value, controllers.routes.ExportController.getExportPage(ExportPages.prices.case_value),
                                  controllers.routes.ExportController.submitExportPage(), "export.date.header")).addingToSession(Prices.toSession(valueInForm, MibTypes.mibExport): _*)
          }
      }
    )
  }

  def get(implicit request: Request[AnyContent]) = {
    Ok(purchase_prices(prices.fill(Prices.fromSession(request.session, MibTypes.mibExport).getOrElse(throw new MibException("Prices details not found"))), prices,
                       ExportPages.prices.toString, controllers.routes.ExportController.submitExportPage()))
  }

}
