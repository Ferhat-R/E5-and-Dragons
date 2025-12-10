package characters

import actions.CombatAction
import rolls.Die.{D4, D6, D8}

enum DndClass:
  case PALADIN(lvl: Int) extends DndClass
  case WIZARD(lvl: Int) extends DndClass
  case ROGUE(lvl: Int) extends DndClass

  def action: CombatAction =
    this match
      case DndClass.PALADIN(_) => CombatAction(2, D6)
      case DndClass.WIZARD(_)  => CombatAction(1, D8)
      case DndClass.ROGUE(_)   => CombatAction(1, D6)

  def bonusAction: Option[CombatAction] =
    this match
      case DndClass.PALADIN(lvl) => if lvl > 3 then Some(CombatAction(1, D6)) else None
      case DndClass.WIZARD(_)    => None
      case DndClass.ROGUE(lvl)   => if lvl > 2 then Some(CombatAction(1, D4)) else None
