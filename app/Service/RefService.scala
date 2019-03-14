package Service

import java.util.concurrent.ThreadLocalRandom

import javax.inject.Singleton

//TODO Neeed a better random unique no solution
@Singleton
class RefService {

  def importRef = "MIBI" + getUniqueNumber

  def exportRef =
    "MIBE" + getUniqueNumber

  private def getUniqueNumber = {

    f"${ThreadLocalRandom.current().nextLong(0L, 9999999999L)}%010d"
  }

}
