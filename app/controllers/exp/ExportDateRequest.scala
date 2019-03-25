package controllers.exp

import service.{CountriesService, WorkingDaysService}
import config.AppConfig
import controllers.FormsConstraints
import controllers.FormsExp._
import controllers.FormsShared._
import javax.inject.{Inject, Singleton}
import model.exp.JourneyDetailsExp
import model.shared.ImportExportDate
import model.{ExportPages, MibTypes}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{AnyContent, Request, Results}
import views.html.exportpages.export_journey_details
import views.html.shared.import_export_date

import scala.concurrent.ExecutionContext

@Singleton
class ExportDateRequest @Inject() (val messagesApi:    MessagesApi,
                                   countriesService:   CountriesService,
                                   workingDaysService: WorkingDaysService)(implicit ec: ExecutionContext, appConfig: AppConfig) extends I18nSupport with Results {

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
          ExportPages.export_date.toString, controllers.routes.ExportController.getExportPage(ExportPages.prices.case_value),
          controllers.routes.ExportController.submitExportPage(), "export.date.header"
        ))
      },
      {
        valueInForm =>
          {

            val exportDateValidate = FormsConstraints.validDate(importExportDate.fill(valueInForm), false, workingDaysService)

            if (exportDateValidate.errors.size == 0) {

              Ok(export_journey_details(JourneyDetailsExp.fromSession(request.session).fold(journeyDetailsExp)(journeyDetailsExp.fill(_)), countriesService.getCountries,
                                        ExportPages.journey_details.case_value, ExportPages.export_date.case_value)).addingToSession(ImportExportDate.toSession(valueInForm, MibTypes.mibExport): _*)

            } else {
              Ok(import_export_date(
                importExportDate.fill(valueInForm),
                exportDateValidate,
                ExportPages.export_date.toString, controllers.routes.ExportController.getExportPage(ExportPages.prices.case_value),
                controllers.routes.ExportController.submitExportPage(), "export.date.header"
              ))
            }
          }
      }
    )

  }

  def get(implicit request: Request[AnyContent]) = {
    Ok(import_export_date(importExportDate.fill(ImportExportDate.fromSession(request.session, MibTypes.mibExport).get), importExportDate,
                          ExportPages.export_date.toString, controllers.routes.ExportController.getExportPage(ExportPages.prices.case_value),
                          controllers.routes.ExportController.submitExportPage(), "export.date.header"))
  }
}
