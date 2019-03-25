package service

import java.time.LocalDate
import java.time.LocalDate.{of => d}

import org.scalatest.{Inspectors, Matchers, WordSpec}

class WorkingDaysServiceSpec extends WordSpec with Matchers with Inspectors {

  private case class Test(date: LocalDate, daysToAdd: Int, expected: LocalDate)

  "addWorkingDays" must {

    "skip over weekends as well as bank holidays" in {

      val tests = Seq[Test](
        Test(date      = d(2019, 12, 24), daysToAdd = 1, expected = d(2019, 12, 27)),
        Test(date      = d(2018, 10, 5), daysToAdd = 1, expected = d(2018, 10, 8)),
        Test(date      = d(2018, 10, 5), daysToAdd = 5, expected = d(2018, 10, 12)),
        Test(date      = d(2018, 10, 5), daysToAdd = 6, expected = d(2018, 10, 15))
      )

      forAll(tests) { test =>

        val service = new WorkingDaysService()
        service.addWorkingDays(test.date, test.daysToAdd) shouldBe test.expected
      }
    }
  }
}
