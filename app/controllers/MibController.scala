package controllers

import config.AppConfig
import javax.inject.{Inject, Singleton}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MibController @Inject() (val messagesApi: MessagesApi)(implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendController with I18nSupport {

  def getSelectDeclarationTypePage(): Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(views.html.select_declaration_type()))
  }

}
