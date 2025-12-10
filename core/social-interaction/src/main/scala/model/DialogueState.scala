package model

case class NpcDialogue(
  npcName: String,
  greeting: String,
  responses: List[String]
)

case class DialogueState(
  npc: NpcDialogue,
  playerResponded: Boolean = false
)
