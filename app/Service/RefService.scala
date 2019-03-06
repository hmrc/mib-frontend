package Service

import javax.inject.Singleton

//TODO Neeed a better random unique no solution
@Singleton
class RefService {

  def importRef = "MIBI" + getUniqueNumber

  def exportRef =
    "MIBE" + getUniqueNumber

  def getUniqueNumber = {

    /**
     *  As of 4/3/2019 MS = 1551716551179  (x)
     *  There are 86400000 ms in a day  (y)
     *  x/y = 17960
     *  we take the first 4 ms in (x) meaning this is a max of 10 days value
     *
     */

    val currentTime4 = System.currentTimeMillis().toString.take(4)
    val r = scala.util.Random
    val next6 = r.nextInt(999999).toString
    currentTime4 + next6
  }

}
