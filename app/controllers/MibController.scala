package controllers

import Service.CountriesService
import config.AppConfig
import javax.inject.{Inject, Singleton}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import controllers.FormsImp._
import controllers.FormsExp._
import model._
import play.api.Logger
import views.html.shared.select_declaration_type
import views.html.importpages.journey_details
import views.html.exportpages.export_journey_details

import scala.concurrent.ExecutionContext
import ResultWrapper._
import model.exp.JourneyDetailsExp
import model.imp.JourneyDetailsImp

@Singleton
class MibController @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService)(implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendController with I18nSupport {

  def getSelectDeclarationTypePage(): Action[AnyContent] = Action { implicit request =>
    Ok(select_declaration_type(controllers.routes.MibController.submitSelectDecTypePage(), selectDecType)).purgeSession
  }

  def getSelectDeclarationTypePageRestart(): Action[AnyContent] = Action { implicit request =>
    Ok(select_declaration_type(controllers.routes.MibController.submitSelectDecTypePage(), selectDecType.fill(SelectDecType.fromSession(request.session).get)))
  }

  def submitSelectDecTypePage(): Action[AnyContent] = Action { implicit request =>
    FormsImp.selectDecType.bindFromRequest().fold(
      formWithErrors => Ok(select_declaration_type(
        controllers.routes.MibController.submitSelectDecTypePage(),
        formWithErrors
      )),
      {

        case SelectDecType(Some("1")) => Ok(journey_details(JourneyDetailsImp.fromSession(request.session).fold(journeyDetailsImp)(journeyDetailsImp.fill(_)),
                                                            countriesService.getCountries, ImportPages.journey_details.case_value))
          .addingToSession(SelectDecType.toSession(SelectDecType(Some("1"))): _*)
        case SelectDecType(Some("2")) => Ok(export_journey_details(JourneyDetailsExp
          .fromSession(request.session).fold(journeyDetailsExp)(journeyDetailsExp.fill(_)), countriesService.getCountries, ExportPages.journey_details.toString))
          .addingToSession(SelectDecType.toSession(SelectDecType(Some("2"))): _*)
      }
    )
  }

}
