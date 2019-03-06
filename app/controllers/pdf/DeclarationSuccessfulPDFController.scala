package controllers.pdf

import connector.PdfGeneratorConnector
import controllers.FormsExp.declarationReceived
import exceptions.MibException
import javax.inject.{Inject, Singleton}
import model.exp.DeclarationReceived
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Request}
import uk.gov.hmrc.http.BadRequestException
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.pdf.declaration_successful_pdf

import scala.concurrent.ExecutionContext

@Singleton
class DeclarationSuccessfulPDFController @Inject() (
    val messagesApi:       MessagesApi,
    pdfGeneratorConnector: PdfGeneratorConnector)(implicit ec: ExecutionContext) extends FrontendController with PDFGeneration with I18nSupport {

  def downloadPDF(): Action[AnyContent] = Action.async { implicit request =>

    val dec = DeclarationReceived.fromSession(request.session).getOrElse(throw new MibException("Declaration data not found"))

    val pdfContent = declaration_successful_pdf.render(dec.currentDate, dec.traderNameAndAddress, dec.description, dec.mibReference, request, request2Messages).toString()
    pdfGeneratorConnector.generatePdf(pdfContent).map { response =>

      response.status match {
        case OK => Ok(response.bodyAsBytes.toArray).as("application/pdf")
          .withHeaders(
            "Content-Disposition" -> s"""attachment; filename="${FileName("reference".toString)}"""").as("application/pdf")
        case _ =>
          {
            throw new BadRequestException(s"""PDF Generation Failed with Body: ${response.body}""")
          }
      }
    }
  }

}

