package in

import actions.{CardinalDirection, NextAction}

trait   ForMovingCharacter:
  def move(cardinalDirection: CardinalDirection): NextAction
