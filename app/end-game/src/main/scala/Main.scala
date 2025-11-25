import domain.{MapManager, MovementEngine}
import data.MutableCollectionDataStorageAdapter
import rendering.ConsoleRenderingAdapter
import actions.CardinalDirection
import scala.io.StdIn
import scala.io.Source

@main def Main(): Unit =
  val storageAdapter   = MutableCollectionDataStorageAdapter()
  val renderingAdapter = ConsoleRenderingAdapter()
  val mapManager       = MapManager(storageAdapter)
  val movementEngine   = MovementEngine(storageAdapter)

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
              println("Use W/A/S/D to move (N/W/S/E), Q to quit.")
              var continue = true
              while continue do
                val input = StdIn.readLine()
                input match
                  case null => continue = false
                  case s if s.equalsIgnoreCase("q") =>
                    continue = false
                  case s if s.equalsIgnoreCase("w") =>
                    movementEngine.move(CardinalDirection.NORTH)
                  case s if s.equalsIgnoreCase("s") =>
                    movementEngine.move(CardinalDirection.SOUTH)
                  case s if s.equalsIgnoreCase("a") =>
                    movementEngine.move(CardinalDirection.WEST)
                  case s if s.equalsIgnoreCase("d") =>
                    movementEngine.move(CardinalDirection.EAST)
                  case _ =>
                    println("Unknown command. Use W/A/S/D to move, Q to quit.")

                // After any move attempt, re-render latest state
                storageAdapter.lastMapState.foreach(renderingAdapter.renderMapState)
            case None =>
              println("WARNING: Map was validated but not stored!")
        case Left(error) =>
          println(s"ERROR: Failed to parse map - $error")
    finally
      source.close()
