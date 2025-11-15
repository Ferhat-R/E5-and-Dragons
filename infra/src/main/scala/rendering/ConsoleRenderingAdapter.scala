package rendering

import model.{DndMap, FightState}
import out.{ExplorationRenderingPortOut, FightRenderingPortOut}

class ConsoleRenderingAdapter extends FightRenderingPortOut, ExplorationRenderingPortOut:
  override def renderFightState(fightState: FightState): Unit = ???

  override def renderMapState(dndMap: DndMap): Unit = ???
