package rendering

import model.{DndMapState, FightState, Position}
import out.{ExplorationRenderingPortOut, FightRenderingPortOut}

class ConsoleRenderingAdapter extends FightRenderingPortOut, ExplorationRenderingPortOut:
  override def renderFightState(fightState: FightState): Unit =
    ()

  override def renderMapState(dndMap: DndMapState): Unit =
    val width  = dndMap.width
    val height = dndMap.height

    val npcPositions: Set[(Int, Int)] = dndMap.npcs.map(p => (p.x, p.y)).toSet

    val fightablePositions: Map[(Int, Int), Int] =
      dndMap.fightableCharacters.zipWithIndex
        .map { case ((_, pos), idx) => (pos.x -> pos.y) -> (idx + 1) }
        .toMap

    val playerPos   = (dndMap.playerCharacter.position.x, dndMap.playerCharacter.position.y)
    val goldByPos: Map[(Int, Int), Int] =
      dndMap.gold.groupMapReduce { case (pos, _) => (pos.x, pos.y) }(_._2)(_ + _)

    def cellSymbol(x: Int, y: Int): String =
      val key = (x, y)

      if key == playerPos then
        "P"
      else if npcPositions.contains(key) then
        "N"
      else if fightablePositions.contains(key) then
        fightablePositions(key).toString
      else if goldByPos.contains(key) then
        "G"
      else
        "."

    println("=" * (width * 2))
    println("Exploration Map:")

    for y <- 0 until height do
      val rowSymbols = (0 until width).map(x => cellSymbol(x, y)).mkString(" ")
      println(rowSymbols)

    println()
    println(s"Player at (${playerPos._1}, ${playerPos._2}) facing ${dndMap.playerCharacter.orientation}")

    if dndMap.fightableCharacters.nonEmpty then
      println("Fightable characters:")
      dndMap.fightableCharacters.zipWithIndex.foreach { case ((char, pos), idx) =>
        println(s"  ${idx + 1}: ${char.dndRace} ${char.dndClass} at (${pos.x}, ${pos.y}) - AC: ${char.armorClass}, HP: ${char.hp}")
      }

    if dndMap.npcs.nonEmpty then
      println("NPCs:")
      dndMap.npcs.foreach { pos =>
        println(s"  - NPC at (${pos.x}, ${pos.y})")
      }

    if dndMap.gold.nonEmpty then
      println("Gold:")
      dndMap.gold.foreach { case (pos, amount) =>
        println(s"  - ${amount} GP at (${pos.x}, ${pos.y})")
      }

    println("=" * (width * 2))
