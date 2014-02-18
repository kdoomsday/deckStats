package ebarrientos.deckStats.stringParsing

import ebarrientos.deckStats.basics.Mana
import ebarrientos.deckStats.basics.XMana
import ebarrientos.deckStats.basics.ColorlessMana
import ebarrientos.deckStats.basics.HybridMana
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.ColorlessMana
import scala.util.parsing.combinator.JavaTokenParsers
import ebarrientos.deckStats.basics.ColorlessMana

/** Mana parser for json results from mtgdb (http://api.mtgdb.info/). */
object MtgDBManaParser extends JavaTokenParsers with ParserHelpers {
  // More concise definitions caused parsing to fail in hybrid mana cases.

  def cost: Parser[Seq[Mana]]    = rep(mana)
  def mana: Parser[Mana]         = (color | colorless | xMana | hybrid)
  def color: Parser[Mana]        = "{" ~> colorLetter <~ "}"
  def colorless: Parser[Mana]    = "{" ~> colorlessNum <~ "}"
  def xMana: Parser[Mana]        = "{" ~> xSpec <~ "}"
  def hybrid: Parser[Mana]       = "{" ~> rep1sep[Mana](manaDef, "/") <~ "}" ^^ (x => HybridMana(x.toSet))
  def manaDef: Parser[Mana]      = colorLetter | colorlessNum | xSpec

  def colorLetter: Parser[Mana]  = ("W" | "U" | "B" | "R" | "G") ^^ (x => str2Mana(x))
  def colorlessNum: Parser[Mana] = wholeNumber ^^ (x => ColorlessMana(x.toInt))
  def xSpec: Parser[Mana]        = "X" ^^ (_ => XMana())
}