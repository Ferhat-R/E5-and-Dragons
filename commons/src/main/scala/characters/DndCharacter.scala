package characters

final case class DndCharacter(dndRace: DndRace, dndClass: DndClass, shout: String, armorClass: Int, hp: Int, gold: Int, potions: Int = 0)
