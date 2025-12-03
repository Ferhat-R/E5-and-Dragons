package domain

import characters.DndCharacter
import errors.Death
import in.{ForFighting, NewCharacterState}
import out.{RandomnessPortOut, FightRenderingPortOut}
import rolls.Die
import model.FightState

class FightingEngine(randomnessPortOut: RandomnessPortOut, renderingPortOut: FightRenderingPortOut) extends ForFighting:
  override def fight(character: DndCharacter, villain: DndCharacter): Either[Death, NewCharacterState] =
    // Initialize combat state
    var playerHp = character.hp
    var villainHp = villain.hp
    
    // Phase 1: Initiative - Roll d20 for both characters
    val playerInitiative = randomnessPortOut.getRandom(Die.D20)
    val villainInitiative = randomnessPortOut.getRandom(Die.D20)
    
    // Determine turn order
    val playerGoesFirst = playerInitiative >= villainInitiative
    
    // Phase 2: Combat loop
    var combatActive = true
    while combatActive do
      // Determine attacker and defender for this round
      val (attacker, defender, attackerHp, defenderHp, isPlayerAttacking) = 
        if playerGoesFirst then
          // Player attacks first, then villain
          (character, villain, playerHp, villainHp, true)
        else
          // Villain attacks first, then player
          (villain, character, villainHp, playerHp, false)
      
      // Process both combatants' turns in order
      for turn <- 0 until 2 if combatActive do
        val (currentAttacker, currentDefender, isCurrentPlayerAttacking) = 
          if (playerGoesFirst && turn == 0) || (!playerGoesFirst && turn == 1) then
            (character, villain, true)
          else
            (villain, character, false)
        
        val currentAttackerHp = if isCurrentPlayerAttacking then playerHp else villainHp
        val currentDefenderHp = if isCurrentPlayerAttacking then villainHp else playerHp
        
        // Skip turn if attacker is already dead
        if currentAttackerHp > 0 then
          // Attack roll - roll d20 to hit
          val attackRoll = randomnessPortOut.getRandom(Die.D20)
          
          if attackRoll >= currentDefender.armorClass then
            // Hit! Calculate damage
            var totalDamage = 0
            
            // Main action damage
            val action = currentAttacker.dndClass.action
            for _ <- 0 until action.diceAmount do
              totalDamage += randomnessPortOut.getRandom(action.diceRoll)
            
            // Bonus action damage (if available)
            currentAttacker.dndClass.bonusAction match
              case Some(bonusAction) =>
                for _ <- 0 until bonusAction.diceAmount do
                  totalDamage += randomnessPortOut.getRandom(bonusAction.diceRoll)
              case None => // No bonus action
            
            // Apply damage
            if isCurrentPlayerAttacking then
              villainHp = Math.max(0, villainHp - totalDamage)
            else
              playerHp = Math.max(0, playerHp - totalDamage)
            
            // Render hit state
            val attackerName = if isCurrentPlayerAttacking then "Player" else "Villain"
            val defenderName = if isCurrentPlayerAttacking then "Villain" else "Player"
            val state = FightState(
              "Player", character, playerHp,
              "Villain", villain, villainHp,
              attackerName,
              s"$attackerName rolled $attackRoll and HIT for $totalDamage damage!"
            )
            renderingPortOut.renderFightState(state)
          else
            // Miss!
            val attackerName = if isCurrentPlayerAttacking then "Player" else "Villain"
            val state = FightState(
              "Player", character, playerHp,
              "Villain", villain, villainHp,
              attackerName,
              s"$attackerName rolled $attackRoll and MISSED!"
            )
            renderingPortOut.renderFightState(state)
          
          // Check for death
          if playerHp <= 0 then
            combatActive = false
          else if villainHp <= 0 then
            combatActive = false
    
    // Phase 3: Resolution
    if playerHp <= 0 then
      Left(Death())
    else
      // Player wins - transfer villain's gold
      val updatedCharacter = character.copy(
        hp = playerHp,
        gold = character.gold + villain.gold
      )
      Right(updatedCharacter)

