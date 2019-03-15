package controllers.imp

import config.AppConfig
import controllers.FormsConstraints
import controllers.FormsImp._
import controllers.FormsShared.{importExportDate, prices}
import exceptions.MibException
import javax.inject.{Inject, Singleton}
import model.imp.PricesTaxesImp
import model.shared.{ImportExportDate, Prices}
import model.{ImportPages, MibTypes}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{AnyContent, Request, Results}
import views.html.importpages.prices_taxes
import views.html.shared.import_export_date

import scala.concurrent.ExecutionContext

@Singleton
class ImportDateRequest @Inject() (val messagesApi: MessagesApi)(implicit ec: ExecutionContext, appConfig: AppConfig) extends I18nSupport with Results {

  def post(implicit request: Request[AnyContent]) = {
    importExportDate.bindFromRequest().fold(
      formWithErrors => {
        val tempForm = importExportDate.bindFromRequest().discardingErrors
        val errorSummary = FormsConstraints.setImportExportDateSummaryError(tempForm, MibTypes.mibImport)
        val newFormWithErrors = errorSummary.errors.count(f => f.message == "error.real.date") match {
          case 0 => formWithErrors
          case _ => formWithErrors.discardingErrors
        }

        Ok(import_export_date(
          newFormWithErrors,
          errorSummary,
          ImportPages.import_date.case_value,
          controllers.routes.ImportController.getImportPage(ImportPages.prices.case_value), controllers.routes.ImportController.submitImportPage(),
          "import.date.header"
        ))
      },
      {
        valueInForm =>
          {

            val importDateValidate = FormsConstraints.validDate(importExportDate.fill(valueInForm), true)

            if (importDateValidate.errors.size == 0) {

              val pricesVal = prices.fill(Prices.fromSession(request.session, MibTypes.mibImport).get)
              Ok(prices_taxes(PricesTaxesImp.fromSession(request.session).fold(pricesTaxesImp)(pricesTaxesImp.fill(_)),
                              ImportPages.prices_taxes.case_value, ImportPages.import_date.case_value, pricesVal.get.purchasePrice.formatted("%,1.2f").toString)).addingToSession(ImportExportDate.toSession(valueInForm, MibTypes.mibImport): _*)
            } else {
              Ok(import_export_date(
                importExportDate.fill(valueInForm),
                importDateValidate,
                ImportPages.import_date.case_value,
                controllers.routes.ImportController.getImportPage(ImportPages.prices.case_value), controllers.routes.ImportController.submitImportPage(),
                "import.date.header"
              ))
            }

          }
      }
    )
  }

  def get(implicit request: Request[AnyContent]) = {
    Ok(import_export_date(importExportDate.fill(ImportExportDate.fromSession(request.session, MibTypes.mibImport).getOrElse(throw new MibException("ImportExport date details not found"))),
                          importExportDate,
                          ImportPages.import_date.case_value,
                          controllers.routes.ImportController.getImportPage(ImportPages.prices.case_value),
                          controllers.routes.ImportController.submitImportPage(),
      "import.date.header"))
  }

}
