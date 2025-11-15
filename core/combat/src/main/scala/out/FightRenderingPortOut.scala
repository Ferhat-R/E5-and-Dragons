package out

import model.FightState

trait FightRenderingPortOut:
  def renderFightState(fightState: FightState): Unit
