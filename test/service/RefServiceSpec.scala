package service

import Service.RefService
import support.ITSpec

class RefServiceSpec extends ITSpec {

  val refService = fakeApplication.injector.instanceOf[RefService]

    "ref-service " - {
      "import " in {

          val imp = refService.importRef
          imp.length shouldBe 14
          imp.take(4) shouldBe "MIBI"
          imp.takeRight(10).matches("[0-9]*") shouldBe true
      }
      "export " in {

        val exp = refService.exportRef
        exp.length shouldBe 14
        exp.take(4) shouldBe "MIBE"
        exp.takeRight(10).matches("[0-9]*") shouldBe true
      }
    }
}
