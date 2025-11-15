package out

import model.DndMap

trait ExplorationRenderingPortOut:
  def renderMapState(dndMap: DndMap): Unit
