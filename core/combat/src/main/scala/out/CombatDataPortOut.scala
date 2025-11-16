package out

import characters.DndCharacter

trait CombatDataPortOut:
  def saveCharacterState(dndCharacter: DndCharacter, villain: DndCharacter): Unit
