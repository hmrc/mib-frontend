package controllers

import org.scalatest.{Matchers, WordSpec}

class FormsConstrainstsSpec extends WordSpec with Matchers {

  case class Data(textToCheck: String, expectedResult: Boolean = true)

  val data = List(Data("£!@$%^&*()_-{}:;|<>,.?/", false),
    Data("1234567890", false),
    Data("abcdefghijklmnopqrstuvwxyz", false),
    Data("ABCDEFGHIJKLMNOPQRSTUVWXYZ", false),
    Data("😀", true),
    Data("🦕", true),
    Data("⛺️", true),
    Data("🧕", true),
    Data("👷 valid text", true),
    Data("👣👅", true),
    Data("🦕 18 The drive", true),
    Data("🧠", true),
    Data("👨🏽‍🎤", true),
    Data("👨🏻‍🏭", true),
    Data("🌹", true),
    Data("☘️", true),
    Data("🍄", true),
    Data("🔥", true),
    Data("💧", true),
    Data("🍴", true),
    Data("🚴‍♂️", true),
    Data("🤽‍♀️", true),
    Data("🚖", true),
    Data("🕹", true),
    Data("📟", true),
    Data("💚", true),
    Data("❤❤❤❤❤❤❤❤❤❤❤❤️", true),
    Data("🂄", true),
    Data("❤︎", true),
    Data("♼", true),
    Data("🐯", true),
    Data("🐭", true),
    Data("🐸", true),
    Data("🙍🏾‍♂️", true),
    Data("👨‍👩‍👧", true),
    Data("🍗", true)

  )

  "Check strings " should {

    for (elem <- data) {
      "check " + elem.textToCheck + " should be " + elem.expectedResult in {
        FormsConstraints.containsEmoji(elem.textToCheck) shouldBe elem.expectedResult
      }
    }

  }

}
