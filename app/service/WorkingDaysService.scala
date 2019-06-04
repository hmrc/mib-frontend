package service

/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.time.LocalDate

import javax.inject.{Inject, Singleton}
import org.joda.time
import play.api.libs.json.OFormat
import uk.gov.hmrc.time.workingdays._
import util.DateHelper.turnJavaTimeToJoda
import util.ResourceReader

import scala.language.postfixOps
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

@Singleton
class WorkingDaysService @Inject() () {

  implicit val hols: BankHolidaySet = BankHolidays()

  def addWorkingDays(date: LocalDate, days: Int): LocalDate = {
    val joda = turnJavaTimeToJoda(date)
    val jodaLocalDate: time.LocalDate = joda.plusWorkingDays(days)
    LocalDate.now().withYear(jodaLocalDate.getYear).withDayOfMonth(jodaLocalDate.getDayOfMonth).withMonth(jodaLocalDate.getMonthOfYear)
  }
}

import play.api.libs.json.Json

object BankHolidays {
  lazy implicit val bankHolidayFmt: OFormat[BankHolidaySet] = Json.format[BankHolidaySet]
  lazy implicit val BankHolidays: OFormat[BankHoliday] = Json.format[BankHoliday]
  type BankHolidaysByRegion = Map[String, BankHolidaySet]

  def apply(): BankHolidaySet = {

    val jsonString = ResourceReader.read("/bank_holidays.json")

    val json = Json.parse(jsonString)
    val map = json.as[BankHolidaysByRegion]
    map.apply("england-and-wales")
  }
}
