package in

import model.DialogueState

trait ForInteracting:
  def interact(): DialogueState
  def processChoice(choice: model.DialogueChoice, character: characters.DndCharacter): (characters.DndCharacter, String)
