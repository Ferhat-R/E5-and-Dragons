package domain

import in.ForGeneratingCharacters
import characters.{DndCharacter, DndClass, DndRace}
import scala.util.Random

class CharacterGenerator extends ForGeneratingCharacters:
  
  private val random = Random()
  
  override def generateRandomCharacter(level: Int): DndCharacter =
    val race = DndRace.HUMAN
    
    generateCharacter(level, race, DndClass.PALADIN(level))
  
  override def generateCharacter(level: Int, race: DndRace, dndClass: DndClass): DndCharacter =
    // Base HP calculation: 10 base + (level - 1) * 6 for Paladin
    val baseHp = dndClass match
      case DndClass.PALADIN(lvl) => 10 + (lvl - 1) * 6
    
    // Armor Class for Paladin (heavy armor)
    val baseAc = 16
    
    // Generate a random battle cry
    val shouts = Array(
      "Pour l'honneur!",
      "Justice!",
      "En avant!",
      "Courage!",
      "Victoire!"
    )
    val randomShout = shouts(random.nextInt(shouts.length))
    
    DndCharacter(
      dndRace = race,
      dndClass = dndClass,
      shout = randomShout,
      armorClass = baseAc,
      hp = baseHp,
      gold = level * 10  // 10 gold per level
    )
