package audit

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class SubmissionRef(value: String)

object SubmissionRef {

  implicit val format: Format[SubmissionRef] = implicitly[Format[String]].inmap(SubmissionRef(_), _.value)
  //implicit val submissionRefFormat: OFormat[SubmissionRef] = Json.format[SubmissionRef]
}
