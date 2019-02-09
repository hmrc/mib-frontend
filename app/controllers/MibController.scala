package controllers

import config.AppConfig
import javax.inject.{Inject, Singleton}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import controllers.Forms._
import model.SelectDecType
import play.api.Logger
import views.html.select_declaration_type
import views.html.partial.radio_option

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MibController @Inject() (val messagesApi: MessagesApi)(implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendController with I18nSupport {

  def getSelectDeclarationTypePage(): Action[AnyContent] = Action { implicit request =>
    Ok(select_declaration_type(controllers.routes.MibController.submitSelectDecTypePage(), selectDecType))
  }

  def submitSelectDecTypePage(): Action[AnyContent] = Action { implicit request =>
    Forms.selectDecType.bindFromRequest().fold(
      formWithErrors => Ok(select_declaration_type(
        controllers.routes.MibController.submitSelectDecTypePage(),
        formWithErrors
      )),
      {
        case SelectDecType(Some("1")) => Redirect(routes.ImportController.getImportPortPage1())
        case SelectDecType(Some("2")) => Redirect(routes.MibController.submitSelectDecTypePage())
      }
    )
  }

}
