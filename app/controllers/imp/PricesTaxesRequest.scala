package controllers.imp

import config.AppConfig
import controllers.FormsConstraints
import controllers.FormsImp.{pricesTaxesImp, taxDueImp}
import controllers.FormsShared.prices
import exceptions.MibException
import javax.inject.{Inject, Singleton}
import model.imp.{PricesTaxesImp, TaxDueImp}
import model.shared.Prices
import model.{ImportPages, MibTypes}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{AnyContent, Request, Results}
import views.html.importpages.{prices_taxes, tax_due}

import scala.concurrent.ExecutionContext

@Singleton
class PricesTaxesRequest @Inject() (val messagesApi: MessagesApi)(implicit ec: ExecutionContext, appConfig: AppConfig) extends I18nSupport with Results {

  def post(implicit request: Request[AnyContent]) = {

    val pricesTaxesObject = Prices.fromSession(request.session, MibTypes.mibImport).getOrElse(throw new MibException("Prices details not found"))
    val pricesVal = prices.fill(pricesTaxesObject)

    pricesTaxesImp.bindFromRequest().fold(
      formWithErrors => {
        Ok(prices_taxes(
          formWithErrors,
          ImportPages.prices_taxes.case_value,
          ImportPages.import_date.case_value,
          pricesVal.get.purchasePrice.formatted("%,1.2f").toString
        ))
      },
      {
        valueInForm =>
          {
            val taxValidate = FormsConstraints.checkTotalTax(pricesTaxesImp.fill(valueInForm), pricesTaxesObject.purchasePrice)
            if (taxValidate.errors.size > 0) {
              Ok(prices_taxes(
                taxValidate,
                ImportPages.prices_taxes.case_value,
                ImportPages.import_date.case_value,
                pricesVal.get.purchasePrice.formatted("%,1.2f").toString
              ))
            } else {
              val due = TaxDueImp(pricesTaxesObject.purchasePrice, valueInForm.customsDuty, valueInForm.importVat, valueInForm.customsDuty + valueInForm.importVat)
              Ok(tax_due(taxDueImp.fill(due), ImportPages.tax_due.case_value, ImportPages.prices_taxes.case_value)).addingToSession(PricesTaxesImp.toSession(valueInForm): _*)
            }
          }
      }
    )

  }

  def get(implicit request: Request[AnyContent]) = {
    val pricesVal = prices.fill(Prices.fromSession(request.session, MibTypes.mibImport).get)
    Ok(prices_taxes(pricesTaxesImp.fill(PricesTaxesImp.fromSession(request.session).get),
                    ImportPages.prices_taxes.case_value, ImportPages.import_date.case_value, pricesVal.get.purchasePrice.toString))
  }
}
