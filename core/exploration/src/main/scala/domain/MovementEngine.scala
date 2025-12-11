package domain

import actions.{CardinalDirection, NextAction}
import in.{ForMovingCharacter, ForInteracting}
import domain.{FightingEngine}
import model.{DndMapState, Position, OrientedCharacter}
import out.ExplorationDataPortOut
import errors.Death

class MovementEngine(
  storage: ExplorationDataPortOut, 
  fightingEngine: FightingEngine,
  socialEngine: ForInteracting
) extends ForMovingCharacter:
  override def move(cardinalDirection: CardinalDirection): NextAction =
    storage.loadMapState() match
      case Some(current) =>
        val moved = movePlayer(current, cardinalDirection)
        
        // Check if player is on an enemy position
        val playerPos = moved.playerCharacter.position
        val enemyAtPosition = moved.fightableCharacters.find { case (_, pos) => 
          pos.x == playerPos.x && pos.y == playerPos.y 
        }
        
        enemyAtPosition match
          case Some((enemy, enemyPos)) =>
            // Combat triggered!
            fightingEngine.fight(moved.playerCharacter.character, enemy) match
              case Right(updatedPlayer) =>
                // Player won! Update player and remove enemy
                val updatedPlayerCharacter = moved.playerCharacter.copy(
                  character = updatedPlayer
                )
                val updatedMap = moved.copy(
                  playerCharacter = updatedPlayerCharacter,
                  fightableCharacters = moved.fightableCharacters.filterNot(_._2 == enemyPos)
                )
                storage.saveMapState(updatedMap)
                NextAction.FIGHT
              case Left(Death()) =>
                // Player died
                println("\nYOU DIED! GAME OVER\n")
                NextAction.FIGHT  // Using FIGHT to signal combat happened, Main will handle death
          case None =>
            // No enemy, check for NPC
            val npcAtPosition = moved.npcs.find { pos =>
              pos.x == playerPos.x && pos.y == playerPos.y
            }
            
            npcAtPosition match
              case Some(_) =>
                // NPC interaction triggered!
                // We don't call interact() here anymore, we let Main handle the loop
                storage.saveMapState(moved)
                NextAction.INTERACT
              case None =>
                // No enemy, no NPC, just save the move
                storage.saveMapState(moved)
                NextAction.MOVE
      case None =>
        NextAction.MOVE

  private def movePlayer(state: DndMapState, dir: CardinalDirection): DndMapState =
    val player = state.playerCharacter

    val (dx, dy) = dir match
      case CardinalDirection.NORTH => (0, -1)
      case CardinalDirection.SOUTH => (0, 1)
      case CardinalDirection.EAST  => (1, 0)
      case CardinalDirection.WEST  => (-1, 0)

    val newX = player.position.x + dx
    val newY = player.position.y + dy

    val clampedX = newX.max(0).min(state.width - 1)
    val clampedY = newY.max(0).min(state.height - 1)

    val newPos = Position(clampedX, clampedY)

    val updatedPlayer = player.copy(position = newPos)
    state.copy(playerCharacter = updatedPlayer)

