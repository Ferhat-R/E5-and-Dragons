package randomness

import out.RandomnessPortOut
import rolls.Die

class MachineDefaultRandomnessAdapter extends RandomnessPortOut:
  override def getRandom(die: Die): Int =
    die match
      case Die.D20 => scala.util.Random.nextInt(20) + 1  // 1-20
      case Die.D6  => scala.util.Random.nextInt(6) + 1   // 1-6

