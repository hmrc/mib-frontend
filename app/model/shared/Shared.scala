package model.shared

import model.{MibType, MibTypes}

trait Shared {

  def appendVal(mibType: MibType) = if (mibType.caseValue.eq(MibTypes.mibExport.caseValue)) "_Exp" else "_Imp"
}
