package in

import model.DialogueState

trait ForInteracting:
  def interact(): DialogueState
