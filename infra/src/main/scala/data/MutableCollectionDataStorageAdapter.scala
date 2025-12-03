package data

import characters.DndCharacter
import model.DndMapState
import out.{CombatDataPortOut, ExplorationDataPortOut}

import scala.collection.mutable.ListBuffer

class MutableCollectionDataStorageAdapter extends ExplorationDataPortOut, CombatDataPortOut:
  private val mapStates: ListBuffer[DndMapState] = ListBuffer.empty
  private val characterStates: ListBuffer[(DndCharacter, DndCharacter)] = ListBuffer.empty

  override def saveMapState(dndMap: DndMapState): Unit =
    mapStates += dndMap

  override def loadMapState(): Option[DndMapState] =
    mapStates.lastOption

  override def saveCharacterState(dndCharacter: DndCharacter, villain: DndCharacter): Unit =
    characterStates += ((dndCharacter, villain))

  def lastMapState: Option[DndMapState] = mapStates.lastOption
  def allMapStates: List[DndMapState] = mapStates.toList

  def lastCharacterState: Option[(DndCharacter, DndCharacter)] = characterStates.lastOption
  def allCharacterStates: List[(DndCharacter, DndCharacter)] = characterStates.toList
