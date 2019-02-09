package controllers

import config.AppConfig
import controllers.Forms._
import javax.inject.{Inject, Singleton}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.import_port_page1

import scala.concurrent.ExecutionContext

@Singleton
class ImportController @Inject() (val messagesApi: MessagesApi)(implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendController with I18nSupport {

  def getImportPortPage1(): Action[AnyContent] = Action { implicit request =>
    Ok(import_port_page1(controllers.routes.ImportController.submitImportPortPage1(), importDec))
  }

  def submitImportPortPage1: Action[AnyContent] = Action { implicit request =>
    Forms.importDec.bindFromRequest().fold(
      formWithErrors => Ok(import_port_page1(
        controllers.routes.ImportController.submitImportPortPage1(),
        formWithErrors
      )),
      {
        case _ => Ok
      }
    )
  }

}
