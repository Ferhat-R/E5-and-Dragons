package domain

import actions.{CardinalDirection, NextAction}
import in.ForMovingCharacter

class MovementEngine() extends ForMovingCharacter:
  override def move(cardinalDirection: CardinalDirection): NextAction = ???
