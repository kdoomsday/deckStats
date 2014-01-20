package ebarrientos.deckStats.stringParsing

import scala.util.parsing.combinator.JavaTokenParsers
import ebarrientos.deckStats.basics.Mana
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.White
import ebarrientos.deckStats.basics.Blue
import ebarrientos.deckStats.basics.Green
import ebarrientos.deckStats.basics.Black
import ebarrientos.deckStats.basics.Red
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.Color
import ebarrientos.deckStats.basics.ColorlessMana
import ebarrientos.deckStats.basics.HybridMana

object ManaParser extends JavaTokenParsers {
	def cost: Parser[List[Mana]] = rep(mana)
	def mana: Parser[Mana] = color | colorless | hybrid
	def color: Parser[Mana] = ("W" | "U" | "B" | "R" | "G") ^^ (x => str2Mana(x))
	def colorless: Parser[Mana] = wholeNumber ^^ (x => ColorlessMana(x.toInt))
	def hybrid: Parser[Mana] = "("~>rep1sep[Mana](mana, "/")<~")" ^^ (x => HybridMana(x.toSet))


	/** Convert a String to Colored Mana */
	private[this] def str2Mana(s: String) = {
	  def str2Color(cs: String): Color = cs match {
	    case "W" => White
	    case "U" => Blue
	    case "B" => Black
	    case "R" => Red
	    case "G" => Green
	    case _ => throw new Exception("Don't know what color this is")
	  }

	  ColoredMana(str2Color(s))
	}
}