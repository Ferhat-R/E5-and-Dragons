package randomness

import out.RandomnessPortOut
import rolls.Die

class MachineDefaultRandomnessAdapter extends RandomnessPortOut:
  override def getRandom(die: Die): Int = ???
