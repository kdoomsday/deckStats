package ebarrientos.deckStats
import ebarrientos.deckStats.stringParsing.ManaParser
import scala.App

object ManaParseTest extends App {
  println(ManaParser.parseAll(ManaParser.cost, "2WW(2/U)"))
}