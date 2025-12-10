package in

import characters.{DndCharacter, DndClass, DndRace}

trait ForGeneratingCharacters:
  def generateRandomCharacter(level: Int): DndCharacter
  def generateCharacter(level: Int, race: DndRace, dndClass: DndClass): DndCharacter
