package controllers

import Service.CountriesService
import config.AppConfig
import controllers.FormsShared._
import controllers.ResultWrapper._
import javax.inject.{Inject, Singleton}
import model._
import model.shared.Prices
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.shared._

import scala.concurrent.ExecutionContext

@Singleton
class MibController @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService)(implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendController with I18nSupport {

  def getSelectDeclarationTypePage(): Action[AnyContent] = Action { implicit request =>
    Ok(select_declaration_type(controllers.routes.MibController.submitSelectDecTypePage(), selectDecType)).purgeSession
  }

  def getSelectDeclarationTypePageRestart(): Action[AnyContent] = Action { implicit request =>
    Ok(select_declaration_type(controllers.routes.MibController.submitSelectDecTypePage(), selectDecType.fill(SelectDecType.fromSession(request.session).get)))
  }

  def submitSelectDecTypePage(): Action[AnyContent] = Action { implicit request =>
    selectDecType.bindFromRequest().fold(
      formWithErrors => Ok(select_declaration_type(
        controllers.routes.MibController.submitSelectDecTypePage(),
        formWithErrors
      )),
      {

        case SelectDecType(Some("1")) => Ok(purchase_prices(Prices.fromSession(request.session, MibTypes.mibImport).fold(prices)(prices.fill(_)), prices,
                                                            ImportPages.prices.case_value, controllers.routes.ImportController.submitImportPage()))
          .addingToSession(SelectDecType.toSession(SelectDecType(Some("1"))): _*)
        case SelectDecType(Some("2")) => Ok(purchase_prices(Prices
          .fromSession(request.session, MibTypes.mibExport).fold(prices)(prices.fill(_)), prices, ExportPages.prices.toString, controllers.routes.ExportController.submitExportPage()))
          .addingToSession(SelectDecType.toSession(SelectDecType(Some("2"))): _*)
      }
    )
  }

}
