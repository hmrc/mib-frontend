package service

import model.Country
import play.api.test.FakeRequest
import support.ITSpec

class CountriesServiceSpec extends ITSpec {

  val countryService = fakeApplication.injector.instanceOf[CountriesService]
  implicit val request = FakeRequest()
  "countryService" - {
    "should not contain the uk " in {
      countryService.getCountries should not contain Country("United Kingdom", "GBR")

    }

    "should Return a country when Given  a code " in {

      countryService.getCountry("GBR") shouldBe "United Kingdom"
    }
  }
}
