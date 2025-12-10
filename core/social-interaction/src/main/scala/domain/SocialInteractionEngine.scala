package domain

import in.ForInteracting
import model.{DialogueState, NpcDialogue, DialogueChoice, CharacterEffect}
import out.SocialRenderingPortOut
import characters.DndCharacter

class SocialInteractionEngine(renderingPortOut: SocialRenderingPortOut) extends ForInteracting:
  
  private val npcDialogues = List(
    NpcDialogue(
      "Marchand ambulant",
      "Salutations ! J'ai des potions de soin pour 50 pièces d'or.",
      List(
        DialogueChoice("Acheter une potion (50 PO)", CharacterEffect.BuyPotion(50)),
        DialogueChoice("Avez-vous vu des monstres ?", CharacterEffect.None),
        DialogueChoice("Au revoir.", CharacterEffect.None)
      )
    ),
    NpcDialogue(
      "Vieux sage",
      "Je peux bénir ton arme pour ton prochain combat, ou soigner tes blessures.",
      List(
        DialogueChoice("Bénissez mon arme (+2 dégâts)", CharacterEffect.BuffDamage(2)),
        DialogueChoice("Soignez-moi (+10 HP)", CharacterEffect.Heal(10)),
        DialogueChoice("Merci, je dois y aller.", CharacterEffect.None)
      )
    ),
    NpcDialogue(
      "Garde mystérieux",
      "Halte ! Si tu veux passer, tu dois être prêt au combat.",
      List(
        DialogueChoice("Je suis prêt !", CharacterEffect.None),
        DialogueChoice("Donnez-moi un conseil.", CharacterEffect.None),
        DialogueChoice("Je pars.", CharacterEffect.None)
      )
    )
  )
  
  override def interact(): DialogueState =
    // Choose a random NPC dialogue
    val randomNpc = npcDialogues(scala.util.Random.nextInt(npcDialogues.length))
    val dialogue = DialogueState(randomNpc, playerResponded = false)
    
    // Render the dialogue
    renderingPortOut.renderDialogue(dialogue)
    
    dialogue

  override def processChoice(choice: DialogueChoice, character: DndCharacter): (DndCharacter, String) =
    choice.effect match
      case CharacterEffect.Heal(amount) =>
        val newHp = Math.min(character.hp + amount, 100) // Cap HP at 100 for now
        (character.copy(hp = newHp), s"Vous avez été soigné de $amount HP !")
        
      case CharacterEffect.BuyPotion(cost) =>
        if character.gold >= cost then
          (character.copy(gold = character.gold - cost, potions = character.potions + 1), "Vous avez acheté une potion !")
        else
          (character, "Pas assez d'or !")
          
      case CharacterEffect.BuffDamage(amount) =>
        // For now, just a message as we don't have temp buffs in DndCharacter yet
        (character, "Votre arme brille d'une énergie mystique ! (Effet non implémenté)")
        
      case CharacterEffect.None =>
        (character, "Très bien.")

