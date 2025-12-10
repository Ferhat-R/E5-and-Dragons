package randomness

import out.RandomnessPortOut
import rolls.Die

class MachineDefaultRandomnessAdapter extends RandomnessPortOut:
  override def getRandom(die: Die): Int =
    die match
      case Die.D20 => scala.util.Random.nextInt(20) + 1
      case Die.D12 => scala.util.Random.nextInt(12) + 1
      case Die.D10 => scala.util.Random.nextInt(10) + 1
      case Die.D8  => scala.util.Random.nextInt(8) + 1
      case Die.D6  => scala.util.Random.nextInt(6) + 1
      case Die.D4  => scala.util.Random.nextInt(4) + 1

