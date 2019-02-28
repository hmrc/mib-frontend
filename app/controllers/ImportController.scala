package controllers

import Service.CountriesService
import config.AppConfig
import controllers.FormsImp._
import controllers.FormsShared._
import javax.inject.{Inject, Singleton}
import model.{ImportPages, MibTypes}
import model.shared._
import model.imp.{_}
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import views.html.importpages._
import views.html.shared._

import scala.concurrent.ExecutionContext

@Singleton
class ImportController @Inject() (val messagesApi: MessagesApi, countriesService: CountriesService)
  (implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendController with I18nSupport {

  //------------------------------------------------------------------------------------------------------------------------------
  def getImportPage(page: String): Action[AnyContent] = Action { implicit request =>

    page match {

      case ImportPages.prices.case_value => Ok(purchase_prices(prices.fill(Prices.fromSession(request.session, MibTypes.mibImport).get),
                                                               ImportPages.prices.case_value, controllers.routes.ImportController.submitImportPage()))

      case ImportPages.import_date.case_value => Ok(import_export_date(importExportDate.fill(ImportExportDate.fromSession(request.session, MibTypes.mibImport).get),
                                                                       ImportPages.import_date.case_value,
                                                                       controllers.routes.ImportController.getImportPage(ImportPages.prices.case_value),
                                                                       controllers.routes.ImportController.submitImportPage(),
        "import.date.header"))

      case ImportPages.prices_taxes.case_value => {
        val pricesVal = prices.fill(Prices.fromSession(request.session, MibTypes.mibImport).get)
        Ok(prices_taxes(pricesTaxesImp.fill(PricesTaxesImp.fromSession(request.session).get),
                        ImportPages.prices_taxes.case_value, ImportPages.import_date.case_value, pricesVal.get.purchasePrice.toString))
      }

      case ImportPages.tax_due.case_value => {
        val prices = PricesTaxesImp.fromSession(request.session).get
        val due = TaxDueImp(prices.purchasePrice, prices.customsDuty, prices.importVat, prices.customsDuty + prices.importVat)
        Ok(tax_due(taxDueImp.fill(due),
                   ImportPages.tax_due.case_value, ImportPages.prices_taxes.case_value))
      }

      case ImportPages.journey_details.case_value => Ok(journey_details(journeyDetailsImp.fill(JourneyDetailsImp.fromSession(request.session).get),
                                                                        countriesService.getCountries, ImportPages.journey_details.case_value, ImportPages.tax_due.case_value))

      case ImportPages.trader_details.case_value =>
        Ok(trader_details(traderDetails.fill(TraderDetails.fromSession(request.session, MibTypes.mibImport).get), countriesService.getCountries,
                          ImportPages.trader_details.case_value,
                          controllers.routes.ImportController.getImportPage(ImportPages.journey_details.case_value), controllers.routes.ImportController.submitImportPage()))

      case ImportPages.merchandise_details.case_value => Ok(merchandise_details(merchandiseDetails.fill(MerchandiseDetails.fromSession(request.session, MibTypes.mibImport).get),
                                                                                ImportPages.merchandise_details.case_value,
                                                                                controllers.routes.ImportController.getImportPage(ImportPages.trader_details.case_value),
                                                                                controllers.routes.ImportController.submitImportPage(), "import.merchandisedetails.desciptionOfGoods"))

      case ImportPages.check_details.case_value => {
        val journey = journeyDetailsImp.fill(JourneyDetailsImp.fromSession(request.session).get)
        val traderFull = traderDetails.fill(TraderDetails.fromSession(request.session, MibTypes.mibImport).get).get

        val merchandise = merchandiseDetails.fill(MerchandiseDetails.fromSession(request.session, MibTypes.mibImport).get)
        val traderCheck = traderDetailsCheckImp.fill(TraderDetailsCheckImp(traderFull.getFormattedAddress(traderFull.country.fold("")(countriesService.getCountry(_))), traderFull.vrn, traderFull.vehicleRegNo))
        val arrival = importExportDate.fill(ImportExportDate.fromSession(request.session, MibTypes.mibImport).get)
        val prices = PricesTaxesImp.fromSession(request.session).get
        val due = taxDueImp.fill(TaxDueImp(prices.purchasePrice, prices.customsDuty, prices.importVat, prices.customsDuty + prices.importVat))
        Ok(check_details(journey,
                         traderCheck,
                         merchandise,
                         arrival,
                         due,
                         countriesService.getCountries,
                         ImportPages.check_details.case_value,
                         ImportPages.prices_taxes.case_value))
      }

    }
  }

  //------------------------------------------------------------------------------------------------------------------------------

  def submitImportPage: Action[AnyContent] = Action { implicit request =>

    val pageno = request.body.asFormUrlEncoded.map(form => {
      val page = form.get("page")
      page.get.head
    })
    pageno match {

      case Some(ImportPages.prices.case_value) => {
        prices.bindFromRequest().fold(
          formWithErrors => Ok(purchase_prices(
            formWithErrors, ImportPages.prices.case_value,
            controllers.routes.ImportController.submitImportPage()
          )),
          {
            valueInForm =>
              {
                Ok(import_export_date(ImportExportDate.fromSession(request.session, MibTypes.mibImport).fold(importExportDate)(importExportDate.fill(_)),
                                      ImportPages.import_date.case_value, controllers.routes.ImportController.getImportPage(ImportPages.prices.case_value),
                                      controllers.routes.ImportController.submitImportPage(),
                  "import.date.header")).addingToSession(Prices.toSession(valueInForm, MibTypes.mibImport): _*)
              }
          }
        )
      }

      //------------
      case Some(ImportPages.import_date.case_value) => {
        importExportDate.bindFromRequest().fold(
          formWithErrors => Ok(import_export_date(
            formWithErrors,
            ImportPages.import_date.case_value,
            controllers.routes.ImportController.getImportPage(ImportPages.prices.case_value), controllers.routes.ImportController.submitImportPage(),
            "import.date.header"
          )),
          {
            valueInForm =>
              {
                val pricesVal = prices.fill(Prices.fromSession(request.session, MibTypes.mibImport).get)
                Ok(prices_taxes(PricesTaxesImp.fromSession(request.session).fold(pricesTaxesImp)(pricesTaxesImp.fill(_)),
                                ImportPages.prices_taxes.case_value, ImportPages.import_date.case_value, pricesVal.get.purchasePrice.toString)).addingToSession(ImportExportDate.toSession(valueInForm, MibTypes.mibImport): _*)
              }
          }
        )
      }
      //------------

      case Some(ImportPages.prices_taxes.case_value) => {
        pricesTaxesImp.bindFromRequest().fold(
          formWithErrors =>
            {
              val pricesVal = prices.fill(Prices.fromSession(request.session, MibTypes.mibImport).get)
              Ok(prices_taxes(
                formWithErrors,
                ImportPages.prices_taxes.case_value,
                ImportPages.import_date.case_value,
                pricesVal.get.purchasePrice.toString
              ))
            },
          {
            valueInForm =>
              {
                val due = TaxDueImp(valueInForm.purchasePrice, valueInForm.customsDuty, valueInForm.importVat, valueInForm.customsDuty + valueInForm.importVat)
                Ok(tax_due(taxDueImp.fill(due), ImportPages.tax_due.case_value, ImportPages.prices_taxes.case_value)).addingToSession(PricesTaxesImp.toSession(valueInForm): _*)
              }
          }
        )
      }

      //------------

      case Some(ImportPages.tax_due.case_value) => {

        Ok(journey_details(JourneyDetailsImp.fromSession(request.session).fold(journeyDetailsImp)(journeyDetailsImp.fill(_)),
                           countriesService.getCountries, ImportPages.journey_details.case_value, ImportPages.tax_due.case_value))
      }

      //------------

      case Some(ImportPages.journey_details.case_value) => {
        journeyDetailsImp.bindFromRequest().fold(
          formWithErrors => Ok(journey_details(
            formWithErrors, countriesService.getCountries,
            ImportPages.journey_details.case_value,
            ImportPages.tax_due.case_value
          )),
          {
            valueInForm =>
              {
                Ok(trader_details(TraderDetails.fromSession(request.session, MibTypes.mibImport).fold(traderDetails)(traderDetails.fill(_)),
                                  countriesService.getCountries, ImportPages.trader_details.case_value,
                                  controllers.routes.ImportController.getImportPage(ImportPages.journey_details.case_value), controllers.routes.ImportController.submitImportPage()))
                  .addingToSession(JourneyDetailsImp.toSession(valueInForm): _*)
              }
          }
        )
      }

      //------------
      case Some(ImportPages.trader_details.case_value) => {
        traderDetails.bindFromRequest().fold(
          formWithErrors => Ok(trader_details(
            formWithErrors, countriesService.getCountries,
            ImportPages.trader_details.case_value, controllers.routes.ImportController.getImportPage(ImportPages.journey_details.case_value), controllers.routes.ImportController.submitImportPage()
          )),
          {
            valueInForm =>
              {
                val postcodeValidate = FormsConstraints.validateTraderDetailsNoPostCodeOrCountry(traderDetails.fill(valueInForm))
                val line1Validate = FormsConstraints.validateTraderDetailsNoLine1(postcodeValidate)

                if (line1Validate.errors.size > 0) {
                  Ok(trader_details(line1Validate,
                                    countriesService.getCountries, ImportPages.trader_details.toString,
                                    controllers.routes.ExportController.getExportPage(ImportPages.journey_details.case_value), controllers.routes.ExportController.submitExportPage()
                  ))
                } else {
                  Ok(merchandise_details(
                    MerchandiseDetails.fromSession(request.session, MibTypes.mibImport).fold(merchandiseDetails)(merchandiseDetails.fill(_)),
                    ImportPages.merchandise_details.case_value,
                    controllers.routes.ImportController.getImportPage(ImportPages.trader_details.case_value),
                    controllers.routes.ImportController.submitImportPage(), "import.merchandisedetails.desciptionOfGoods"
                  )).addingToSession(TraderDetails.toSession(valueInForm, MibTypes.mibImport): _*)
                }
              }
          }
        )
      }

      //------------
      case Some(ImportPages.merchandise_details.case_value) => {
        merchandiseDetails.bindFromRequest().fold(
          formWithErrors => Ok(merchandise_details(
            formWithErrors,
            ImportPages.merchandise_details.case_value,
            controllers.routes.ImportController.getImportPage(ImportPages.trader_details.case_value),
            controllers.routes.ImportController.submitImportPage(), "import.merchandisedetails.desciptionOfGoods"
          )),
          {
            valueInForm =>
              {
                val journey = journeyDetailsImp.fill(JourneyDetailsImp.fromSession(request.session).get)
                val traderFull = traderDetails.fill(TraderDetails.fromSession(request.session, MibTypes.mibImport).get).get

                val merchandise = merchandiseDetails.fill(valueInForm)
                val traderCheck = traderDetailsCheckImp.fill(TraderDetailsCheckImp(traderFull.getFormattedAddress(traderFull.country.fold("")(countriesService.getCountry(_))), traderFull.vrn, traderFull.vehicleRegNo))
                val arrival = importExportDate.fill(ImportExportDate.fromSession(request.session, MibTypes.mibImport).get)
                val prices = PricesTaxesImp.fromSession(request.session).get
                val due = taxDueImp.fill(TaxDueImp(prices.purchasePrice, prices.customsDuty, prices.importVat, prices.customsDuty + prices.importVat))
                Ok(check_details(journey,
                                 traderCheck,
                                 merchandise,
                                 arrival,
                                 due,
                                 countriesService.getCountries,
                                 ImportPages.check_details.case_value,
                                 ImportPages.merchandise_details.case_value))
                  .addingToSession(MerchandiseDetails.toSession(valueInForm, MibTypes.mibImport): _*)
              }
          }
        )
      }

      //------------
      case Some(ImportPages.check_details.case_value) => {

        Ok
      }
      //------------
    }
  }

}
