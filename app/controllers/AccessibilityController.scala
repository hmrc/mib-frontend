package controllers

import config.AppConfig
import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.statement.accessibility_statement

import scala.concurrent.ExecutionContext

class AccessibilityController @Inject() (mcc: MessagesControllerComponents)(implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendController(mcc) {

  def getAccessibilityStatement(): Action[AnyContent] = Action { implicit request =>
    Ok(accessibility_statement())
  }
}
