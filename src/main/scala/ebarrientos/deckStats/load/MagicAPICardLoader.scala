package ebarrientos.deckStats.load

import ebarrientos.deckStats.basics.Card
import scala.util.control.Exception

/** Cardloader that gets card info from http://stegriff.co.uk/ */
class MagicAPICardLoader extends CardLoader with LoadUtils {

  def card(name: String) = {
    println(s"Loading: $name")
    val map = cardMap(name)

    if (map.isDefined) cardFromMap(name, map.get)
    else {
      throw new Exception("Couldn't load card: " + name)
      // TODO Localize exceptions
    }
  }



  /** Load a map with the json bits of the card from the web api. The map has the empty string set
    * as the default value.
    * If there was an error loading, or the card doesn't exist, returns None. Otherwise, returns a
    * Some with the map that contains all relevant values.
    */
  private[this] def cardMap(name: String): Option[Map[String, String]] = {
    import scala.io.Source
    import util.parsing.json.JSON

    val saneName = name replace (" ", "%20")
    val cardStr = Source.fromURL(s"""http://stegriff.co.uk/host/magic/?name=$saneName""").mkString
    val parsed = JSON.parseFull(cardStr)

    parsed flatMap (m => {
      val castMap = m.asInstanceOf[Map[String, String]].withDefaultValue("")
      if (castMap.contains("error")) None
      else Some(castMap)
    })
  }


  private[this] def cardFromMap(name: String, map: Map[String, String]): Card = {
    import ebarrientos.deckStats.stringParsing.MagicApiManaParser.{parseAll, cost}

    val (supertypes, types, subtypes) = parseTypes(map("types"))
    val (power, toughness) = parsePT( map("power_toughness") )

    Card (
        parseAll(cost, map("mana_cost")).get,
        name,
        types,
        supertypes,
        subtypes,
        map("card_text"),
        power,
        toughness
    )
  }
}