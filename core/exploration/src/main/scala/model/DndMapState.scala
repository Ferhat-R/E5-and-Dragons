package model

import characters.DndCharacter
import actions.CardinalDirection

case class Position(x: Int, y: Int)

case class OrientedCharacter(character: DndCharacter, position: Position, orientation: CardinalDirection)

case class DndMapState(
  width: Int,
  height: Int,
  npcs: List[Position],
  fightableCharacters: List[(DndCharacter, Position)],
  playerCharacter: OrientedCharacter,
  gold: List[(Position, Int)]
)
