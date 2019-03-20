package controllers

import java.time.LocalDate

import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import controllers.pdf.DeclarationSuccessfulPDFController
import exceptions.MibException
import model.TestData.testDeclaration
import model.exp.DeclarationReceived.toSession
import org.scalatest.concurrent.ScalaFutures
import play.api.test.FakeRequest
import support.ITSpec
class DeclarationSuccessfulPDFControllerSpec extends ITSpec with ScalaFutures {
  def stubPdfGenerator(): StubMapping = {
    stubFor(
      post(urlPathEqualTo("/pdf-generator-service/generate"))
        .willReturn(aResponse().withStatus(200).withBody("")))
  }

  val declarationService: DeclarationSuccessfulPDFController = fakeApplication.injector.instanceOf[DeclarationSuccessfulPDFController]
  "declarationService" - {
    "should throw an exception if Declaration data is not in the session in" in {
      intercept[MibException] { declarationService.downloadPDF()(FakeRequest()) }.message shouldBe "Declaration data not found"
    }

    "should return a 200" in {
      stubPdfGenerator()
      val result = declarationService.downloadPDF()(FakeRequest().withSession(toSession(testDeclaration): _*)).futureValue
      result.header.status shouldBe 200
    }

  }
}
