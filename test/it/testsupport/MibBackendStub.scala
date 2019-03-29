package it.testsupport

import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import play.api.http.Status
object MibBackendStub {
  private val pathExport = "/mib-backend/store/export"
  private val pathImport = "/mib-backend/store/import"
  def storeExport(): StubMapping = {
    stubFor(
      post(urlPathEqualTo(pathExport))
        .willReturn(
          aResponse()
            .withBody("\"Inserted MIBE3786828426\"")
            .withStatus(Status.CREATED)))
  }
  def storeImport(): StubMapping = {
    stubFor(
      post(urlPathEqualTo(pathImport))
        .willReturn(
          aResponse()
            .withBody(
              "\"Inserted MIBE3786828426\"")
            .withStatus(Status.CREATED)))
  }

}
