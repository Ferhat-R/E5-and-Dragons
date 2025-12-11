import actions.NextAction
import domain.{MapManager, MovementEngine, FightingEngine, SocialInteractionEngine}
import data.MutableCollectionDataStorageAdapter
import rendering.ConsoleRenderingAdapter
import randomness.MachineDefaultRandomnessAdapter
import actions.CardinalDirection
import scala.io.StdIn
import scala.io.Source

@main def Main(): Unit =
  val storageAdapter    = MutableCollectionDataStorageAdapter()
  val renderingAdapter  = ConsoleRenderingAdapter()
  val randomnessAdapter = MachineDefaultRandomnessAdapter()
  val mapManager        = MapManager(storageAdapter)
  val fightingEngine    = FightingEngine(randomnessAdapter, renderingAdapter)
  val socialEngine      = SocialInteractionEngine(renderingAdapter)
  val movementEngine    = MovementEngine(storageAdapter, fightingEngine, socialEngine)

  val mapFile = this.getClass.getClassLoader.getResourceAsStream("e5-dungeon.dndmap")

  if mapFile == null then
    println("ERROR: Could not find e5-dungeon.dndmap in resources")
  else
    val source = Source.fromInputStream(mapFile)
    try
      val lines = source.getLines().toList

      mapManager.validateAndStoreMap(lines) match
        case Right(_) =>
          storageAdapter.lastMapState match
            case Some(mapState) =>
              // Initial render
              renderingAdapter.renderMapState(mapState)

              // Simple keyboard loop: WASD and arrow-like keys via letters
              println("Use W/A/S/D to move, I to use potion, Q to quit.")
              var continue = true
              while continue do
                val input = StdIn.readLine()
                
                val actionResult = input match
                  case null => 
                    continue = false
                    NextAction.MOVE
                  case s if s.equalsIgnoreCase("q") =>
                    continue = false
                    NextAction.MOVE
                  case s if s.equalsIgnoreCase("w") =>
                    movementEngine.move(CardinalDirection.NORTH)
                  case s if s.equalsIgnoreCase("s") =>
                    movementEngine.move(CardinalDirection.SOUTH)
                  case s if s.equalsIgnoreCase("a") =>
                    movementEngine.move(CardinalDirection.WEST)
                  case s if s.equalsIgnoreCase("d") =>
                    movementEngine.move(CardinalDirection.EAST)
                  case s if s.equalsIgnoreCase("i") =>
                    // Use potion logic
                    storageAdapter.lastMapState.foreach { currentState =>
                      val player = currentState.playerCharacter.character
                      if player.potions > 0 then
                        val newHp = Math.min(player.hp + 10, 100)
                        val newPlayer = player.copy(hp = newHp, potions = player.potions - 1)
                        val updatedMap = currentState.copy(
                          playerCharacter = currentState.playerCharacter.copy(character = newPlayer)
                        )
                        storageAdapter.saveMapState(updatedMap)
                        println(s"Vous avez utilisé une potion ! HP: $newHp, Potions restantes: ${player.potions - 1}")
                      else
                        println("Vous n'avez pas de potions !")
                    }
                    NextAction.MOVE
                  case _ =>
                    println("Unknown command. Use W/A/S/D to move, I for potion, Q to quit.")
                    NextAction.MOVE

                // Handle interaction if needed
                if actionResult == NextAction.INTERACT then
                  val dialogue = socialEngine.interact()
                  var inDialogue = true
                  while inDialogue do
                    println("Votre choix (numéro) : ")
                    val choiceInput = StdIn.readLine()
                    try
                      val choiceIdx = choiceInput.toInt - 1
                      if choiceIdx >= 0 && choiceIdx < dialogue.npc.choices.length then
                        val choice = dialogue.npc.choices(choiceIdx)
                        
                        // Process choice effect
                        storageAdapter.lastMapState.foreach { currentState =>
                          val (updatedChar, message) = socialEngine.processChoice(choice, currentState.playerCharacter.character)
                          println(s">> $message")
                          
                          // Update player state
                          val updatedMap = currentState.copy(
                            playerCharacter = currentState.playerCharacter.copy(character = updatedChar)
                          )
                          storageAdapter.saveMapState(updatedMap)
                        }
                        inDialogue = false
                      else
                        println("Choix invalide.")
                    catch
                      case _: NumberFormatException => println("Veuillez entrer un numéro.")

                // After any move attempt, re-render latest state
                storageAdapter.lastMapState.foreach(renderingAdapter.renderMapState)
            case None =>
              println("WARNING: Map was validated but not stored!")
        case Left(error) =>
          println(s"ERROR: Failed to parse map - $error")
    finally
      source.close()
