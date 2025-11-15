package domain

import characters.{CardinalDirection, NextAction}
import in.ForMovingCharacter

class MovementEngine() extends ForMovingCharacter:
  override def move(cardinalDirection: CardinalDirection): NextAction = ???
