package filters

import javax.inject.Inject
import play.http.DefaultHttpFilters

class MibFilters @Inject() (
    csrfFilter:      MibSessionIdCheck,
    frontendFilters: uk.gov.hmrc.play.bootstrap.filters.FrontendFilters)
  extends DefaultHttpFilters(csrfFilter +: frontendFilters.filters: _*)
