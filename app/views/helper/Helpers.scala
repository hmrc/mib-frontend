package views.helper

import play.api.data.Form
import play.api.i18n.Messages

object Helpers {

  def titleMakerWithForm(form: Form[_], titleMessage: String)(implicit messages: Messages): String = {

    val title = s"""${Messages(titleMessage)}"""

    if (form.hasErrors) {
      s"""${Messages("global.error-prefix")} $title"""
    } else title

  }

}
