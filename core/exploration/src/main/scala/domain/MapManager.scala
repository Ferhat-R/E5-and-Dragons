package domain

import errors.MapError
import errors.MapError.IllegalMapFormat
import in.ForValidatingMap
import actions.CardinalDirection

class MapManager() extends ForValidatingMap:
  override def validateAndStoreMap(dataLines: List[String]): Either[MapError, Unit] =
    val parsed: Either[MapError, List[Option[ParsedElement]]] =
      dataLines.foldLeft[Either[MapError, List[Option[ParsedElement]]]](Right(Nil)) { (accE, line) =>
        for
          acc   <- accE
          parsed <- parseDataLine(line)
        yield parsed :: acc
      }

    parsed.flatMap { elemsReversed =>
      val elems = elemsReversed.reverse
      val hasMap = elems.exists {
        case Some(MapDimensions(_, _)) => true
        case _                         => false
      }
      if hasMap then Right(()) else Left(IllegalMapFormat())
    }

  sealed trait ParsedElement
  private case class MapDimensions(width: Int, height: Int) extends ParsedElement
  private case class NpcAt(x: Int, y: Int) extends ParsedElement
  private case class PcAt(x: Int, y: Int, lvl: Int, race: characters.DndRace, cls: characters.DndClass, ac: Int, hp: Int) extends ParsedElement
  private case class CharacterAt(x: Int, y: Int, lvl: Int, race: characters.DndRace, cls: characters.DndClass, ac: Int, hp: Int, orientation: CardinalDirection) extends ParsedElement
  private case class GoldAt(x: Int, y: Int, amount: Int) extends ParsedElement

  def parseDataLine(line: String): Either[MapError, Option[ParsedElement]] =
    val trimmed = line.trim

    if trimmed.isEmpty then return Right(None)
    if trimmed.startsWith("--") then return Right(None)

    val rawTokens = trimmed.split("-").map(_.trim).filter(_.nonEmpty).toList

    rawTokens match
      case Nil => Right(None)
      case code :: rest =>
        code.toUpperCase match
          case "M"   => parseMap(rest)
          case "NPC" => parseNpc(rest)
          case "PC"  => parsePc(rest)
          case "C"   => parseCharacter(rest)
          case "GP"  => parseGold(rest)
          case _      => Left(IllegalMapFormat())

  private def parseInts(parts: List[String]): Option[List[Int]] =
    val parsed = parts.map(s => s.toIntOption)
    if parsed.forall(_.isDefined) then Some(parsed.flatten) else None

  private def parseMap(parts: List[String]): Either[MapError, Option[ParsedElement]] =
    if parts.length != 2 then Left(IllegalMapFormat())
    else
      parseInts(parts) match
        case Some(List(width, height)) => Right(Some(MapDimensions(width, height)))
        case _                         => Left(IllegalMapFormat())

  private def parseNpc(parts: List[String]): Either[MapError, Option[ParsedElement]] =
    if parts.length != 2 then Left(IllegalMapFormat())
    else
      parseInts(parts) match
        case Some(List(x, y)) => Right(Some(NpcAt(x, y)))
        case _                => Left(IllegalMapFormat())

  private def parsePc(parts: List[String]): Either[MapError, Option[ParsedElement]] =
    if parts.length != 7 then Left(IllegalMapFormat())
    else
      val (coordPart, rest) = parts.splitAt(2)
      val (lvlPart :: raceStr :: classStr :: acPart :: hpPart :: Nil) = rest
      (for
        coords <- parseInts(coordPart)
        List(x, y) = coords
        lvl    <- lvlPart.toIntOption
        ac     <- acPart.toIntOption
        hp     <- hpPart.toIntOption
        race   <- parseRace(raceStr)
        cls    <- parseClass(classStr, lvl)
      yield PcAt(x, y, lvl, race, cls, ac, hp)) match
        case Some(pc) => Right(Some(pc))
        case None     => Left(IllegalMapFormat())

  private def parseCharacter(parts: List[String]): Either[MapError, Option[ParsedElement]] =
    if parts.length != 8 then Left(IllegalMapFormat())
    else
      val (coordPart, rest) = parts.splitAt(2)
      val (lvlPart :: raceStr :: classStr :: acPart :: hpPart :: orientationStr :: Nil) = rest
      (for
        coords <- parseInts(coordPart)
        List(x, y) = coords
        lvl    <- lvlPart.toIntOption
        ac     <- acPart.toIntOption
        hp     <- hpPart.toIntOption
        race   <- parseRace(raceStr)
        cls    <- parseClass(classStr, lvl)
        orient <- parseOrientation(orientationStr)
      yield CharacterAt(x, y, lvl, race, cls, ac, hp, orient)) match
        case Some(c) => Right(Some(c))
        case None    => Left(IllegalMapFormat())

  private def parseGold(parts: List[String]): Either[MapError, Option[ParsedElement]] =
    if parts.length != 3 then Left(IllegalMapFormat())
    else
      val (coordPart, amountPart :: Nil) = parts.splitAt(2)
      (for
        coords <- parseInts(coordPart)
        List(x, y) = coords
        amount <- amountPart.toIntOption
      yield GoldAt(x, y, amount)) match
        case Some(gp) => Right(Some(gp))
        case None     => Left(IllegalMapFormat())

  private def parseRace(value: String): Option[characters.DndRace] =
    value.toUpperCase match
      case "HUMAN" => Some(characters.DndRace.HUMAN)
      case _        => None

  private def parseClass(value: String, lvl: Int): Option[characters.DndClass] =
    value.toUpperCase match
      case "PALADIN" => Some(characters.DndClass.PALADIN(lvl))
      case _          => None

  private def parseOrientation(value: String): Option[CardinalDirection] =
    value.toUpperCase match
      case "N" | "NORTH" => Some(CardinalDirection.NORTH)
      case "S" | "SOUTH" => Some(CardinalDirection.SOUTH)
      case "E" | "EAST"  => Some(CardinalDirection.EAST)
      case "W" | "WEST"  => Some(CardinalDirection.WEST)
      case _               => None
