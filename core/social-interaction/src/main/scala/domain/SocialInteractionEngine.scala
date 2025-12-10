package domain

import in.ForInteracting
import model.{DialogueState, NpcDialogue}
import out.SocialRenderingPortOut

class SocialInteractionEngine(renderingPortOut: SocialRenderingPortOut) extends ForInteracting:
  
  private val npcDialogues = List(
    NpcDialogue(
      "Marchand ambulant",
      "Salutations, voyageur ! Je parcours ces donjons depuis des années...",
      List(
        "Que vendez-vous ?",
        "Avez-vous vu des monstres par ici ?",
        "Au revoir."
      )
    ),
    NpcDialogue(
      "Vieux sage",
      "Ah, un aventurier ! Laisse-moi te donner un conseil : méfie-toi des ombres.",
      List(
        "Quel genre de conseil pouvez-vous me donner ?",
        "Connaissez-vous des secrets sur ce donjon ?",
        "Merci, je dois y aller."
      )
    ),
    NpcDialogue(
      "Garde mystérieux",
      "Halte ! Qui va là ? ... Oh, un simple aventurier. Passe ton chemin.",
      List(
        "Que gardez-vous ?",
        "Y a-t-il un danger à proximité ?",
        "D'accord, je pars."
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
