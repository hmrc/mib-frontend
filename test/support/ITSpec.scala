package support

import com.google.inject.AbstractModule
import it.testsupport.WireMockSupport
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, FreeSpecLike, Matchers}
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import play.api.inject.guice.{GuiceApplicationBuilder, GuiceableModule}

/**
 * This is common spec for every test case which brings all of useful routines we want to use in our scenarios.
 */

trait ITSpec
  extends FreeSpecLike
  with MockitoSugar
  with BeforeAndAfterEach
  with GuiceOneServerPerTest
  with Matchers
  with WireMockSupport {

  val overridingsModule = new AbstractModule {
    override def configure(): Unit = ()
  }

  override def fakeApplication = new GuiceApplicationBuilder()
    .overrides(GuiceableModule.fromGuiceModules(Seq(overridingsModule)))
    .configure(
      "microservice.services.pdf-generator-service.host" -> WireMockSupport.host,
      "microservice.services.pdf-generator-service.port" -> WireMockSupport.port,
      "microservice.services.pay-api.host" -> WireMockSupport.host,
      "microservice.services.pay-api.port" -> WireMockSupport.port
    ).build()
}
