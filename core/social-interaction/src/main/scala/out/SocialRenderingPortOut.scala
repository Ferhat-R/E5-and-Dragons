package out

import model.DialogueState

trait SocialRenderingPortOut:
  def renderDialogue(dialogue: DialogueState): Unit
