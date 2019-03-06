package controllers.pdf

import java.util.concurrent.Executors

import org.apache.commons.io.output.ByteArrayOutputStream
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.xhtmlrenderer.pdf.ITextRenderer
import play.api.i18n.I18nSupport

import scala.concurrent.{ExecutionContext, Future}

trait PDFGeneration extends I18nSupport {

  val executionCtx = ExecutionContext.fromExecutor(Executors.newWorkStealingPool())

  protected def generatePdf(content: String): Future[Array[Byte]] = Future {
    val os = new ByteArrayOutputStream()
    val pdfRenderer = new ITextRenderer()

    pdfRenderer.setDocumentFromString(content)
    pdfRenderer.layout()
    pdfRenderer.createPDF(os, true)

    os.toByteArray
  }(executionCtx)

  protected object FileName {
    def apply(taxType: String): String = {
      val formattedDate = DateTimeFormat.forPattern("dd MMMM yyyy").print(DateTime.now())
      s"$taxType payment $formattedDate.pdf"
    }
  }
}
