package controllers

import Service.CountriesService
import config.AppConfig
import javax.inject.{Inject, Singleton}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import controllers.Forms._
import model._
import play.api.Logger
import views.html.select_declaration_type
import views.html.importpages.journey_details

import scala.concurrent.ExecutionContext
import ResultWrapper._
@Singleton
class MibController @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService)(implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendController with I18nSupport {

  def getSelectDeclarationTypePage(): Action[AnyContent] = Action { implicit request =>
    Ok(select_declaration_type(controllers.routes.MibController.submitSelectDecTypePage(), selectDecType)).purgeSession
  }

  def getSelectDeclarationTypePageRestart(): Action[AnyContent] = Action { implicit request =>
    Ok(select_declaration_type(controllers.routes.MibController.submitSelectDecTypePage(), selectDecType.fill(SelectDecType.fromSession(request.session).get)))
  }

  def submitSelectDecTypePage(): Action[AnyContent] = Action { implicit request =>
    Forms.selectDecType.bindFromRequest().fold(
      formWithErrors => Ok(select_declaration_type(
        controllers.routes.MibController.submitSelectDecTypePage(),
        formWithErrors
      )),
      {

        case SelectDecType(Some("1")) => Ok(journey_details(JourneyDetails.fromSession(request.session).fold(journeyDetails)(journeyDetails.fill(_)), countriesService.getCountries))
          .addingToSession(SelectDecType.toSession(SelectDecType(Some("1"))): _*)
        case SelectDecType(Some("2")) => Redirect(routes.MibController.submitSelectDecTypePage()).purgeSession
      }
    )
  }

}
