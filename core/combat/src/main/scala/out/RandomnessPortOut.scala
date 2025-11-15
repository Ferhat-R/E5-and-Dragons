package out

import rolls.Die

trait RandomnessPortOut:
  def getRandom(die: Die): Int
