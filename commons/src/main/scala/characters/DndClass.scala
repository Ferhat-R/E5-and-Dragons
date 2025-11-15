package characters

import rolls.Die.D6

enum DndClass:
  case PALADIN(lvl: Int) extends DndClass

  def action: Action =
    this match
      case DndClass.PALADIN(_) => Action(2, D6)

  def bonusAction: Option[Action] =
    this match
      case DndClass.PALADIN(lvl) => if lvl > 3 then Some(Action(1, D6)) else None
