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
    // Base HP calculation
    val baseHp = dndClass match
      case DndClass.PALADIN(lvl) => 10 + (lvl - 1) * 6
      case DndClass.WIZARD(lvl)  => 6 + (lvl - 1) * 4
      case DndClass.ROGUE(lvl)   => 8 + (lvl - 1) * 5
    
    // Armor Class
    val baseAc = dndClass match
      case DndClass.PALADIN(_) => 16
      case DndClass.WIZARD(_)  => 12
      case DndClass.ROGUE(_)   => 14
    
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
