package filters

import java.util.UUID

import akka.stream.Materializer
import javax.inject.Inject
import org.joda.time.{DateTime, DateTimeZone}
import play.api.mvc
import play.api.mvc.{Filter, RequestHeader}
import filters.MibSessionIdCheck.sessionIdKey
import scala.concurrent.{ExecutionContext, Future}

class MibSessionIdCheck @Inject() ()(implicit val mat: Materializer, ex: ExecutionContext)
  extends Filter {
  override def apply(f: RequestHeader => Future[mvc.Result])(rh: RequestHeader): Future[mvc.Result] = f(filteredHeaders1(rh))

  private def filteredHeaders1(rh: RequestHeader, now: () => DateTime = () => DateTime.now.withZone(DateTimeZone.UTC)) =
    if (rh.headers.get(sessionIdKey).isEmpty)
      rh.copy(headers = rh.headers.add(sessionIdKey -> s"$sessionIdKey-${UUID.randomUUID}"))
    else rh
}

object MibSessionIdCheck {
  val sessionIdKey = "mib-sessionId"
}
