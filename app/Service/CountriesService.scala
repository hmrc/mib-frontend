package Service

import javax.inject.{Inject, Singleton}
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{AnyContent, Request}
import model.Country

@Singleton
class CountriesService @Inject() (val messagesApi: MessagesApi) extends I18nSupport {

  private val pattern = "([A-Z]{3})=(.*)".r

  def getCountriesListLang(implicit request: Request[AnyContent]) =
    scala.io.Source.fromFile(Messages("country-code.path")).getLines().map {
      case pattern(code, name) => Country(name = name, code = code)
    }.toSeq

  def getCountries(implicit request: Request[AnyContent]): Seq[Country] = {
    val UK: (Country) => Boolean = c => c.code == "GBR"
    val countries = getCountriesListLang
    countries.filter(UK) ++ countries.filterNot(UK).sortWith((x, y) => x.name.compareTo(y.name) < 0)
  }

  def getCountry(countryCode: String)(implicit request: Request[AnyContent]): String =
    scala.io.Source.fromFile(Messages("country-code.path")).getLines().map {
      case pattern(code, name) => Country(name = name, code = code)
    }.toSeq.filter(c => c.code.equals(countryCode)).head.name
}

