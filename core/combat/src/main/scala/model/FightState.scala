package model

import characters.DndCharacter

case class FightState(
  character1Name: String,
  character1: DndCharacter,
  character1CurrentHp: Int,
  character2Name: String,
  character2: DndCharacter,
  character2CurrentHp: Int,
  currentTurn: String,
  lastAction: String
)
