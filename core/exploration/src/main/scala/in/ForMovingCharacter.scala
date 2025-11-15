package in

import characters.{CardinalDirection, NextAction}

trait   ForMovingCharacter:
  def move(cardinalDirection: CardinalDirection): NextAction
