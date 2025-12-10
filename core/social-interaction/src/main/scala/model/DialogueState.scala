package model

import characters.DndCharacter

enum CharacterEffect:
  case Heal(amount: Int)
  case BuyPotion(cost: Int)
  case BuffDamage(amount: Int)
  case None

case class DialogueChoice(text: String, effect: CharacterEffect)

case class NpcDialogue(
  npcName: String,
  greeting: String,
  choices: List[DialogueChoice]
)

case class DialogueState(
  npc: NpcDialogue,
  playerResponded: Boolean = false
)
