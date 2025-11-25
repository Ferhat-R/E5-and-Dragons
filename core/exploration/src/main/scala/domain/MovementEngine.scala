package domain

import actions.{CardinalDirection, NextAction}
import in.ForMovingCharacter
import model.{DndMapState, Position, OrientedCharacter}
import out.ExplorationDataPortOut

class MovementEngine(storage: ExplorationDataPortOut) extends ForMovingCharacter:
  override def move(cardinalDirection: CardinalDirection): NextAction =
    storage.loadMapState() match
      case Some(current) =>
        val moved = movePlayer(current, cardinalDirection)
        storage.saveMapState(moved)
        NextAction.MOVE
      case None =>
        NextAction.MOVE

  private def movePlayer(state: DndMapState, dir: CardinalDirection): DndMapState =
    val player = state.playerCharacter

    val (dx, dy) = dir match
      case CardinalDirection.NORTH => (0, -1)
      case CardinalDirection.SOUTH => (0, 1)
      case CardinalDirection.EAST  => (1, 0)
      case CardinalDirection.WEST  => (-1, 0)

    val newX = player.position.x + dx
    val newY = player.position.y + dy

    val clampedX = newX.max(0).min(state.width - 1)
    val clampedY = newY.max(0).min(state.height - 1)

    val newPos = Position(clampedX, clampedY)

    val updatedPlayer = player.copy(position = newPos)
    state.copy(playerCharacter = updatedPlayer)
