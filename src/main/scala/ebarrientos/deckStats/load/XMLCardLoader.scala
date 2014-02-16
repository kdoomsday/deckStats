package ebarrientos.deckStats.load
import ebarrientos.deckStats.basics.CardType
import ebarrientos.deckStats.basics.Supertype
import ebarrientos.deckStats.stringParsing.ManaParser
import ebarrientos.deckStats.basics.Card

/** CardLoader that takes its info from an XML file. */
class XMLCardLoader(xmlFile: String) extends CardLoader with LoadUtils {
	private[this] lazy val cards = scala.xml.XML.load(xmlFile)


	def card(name: String): Card = {
	  // The xml find gives nodeSeq. Names are unique, so head gives only match
	  // TODO Handle case where card doesn't exist
	  val seq = (cards \\ "card").filter(x => (x \\ "name").text == name)

	  if (!seq.isEmpty) {
		  val elem = seq.head
		  val name = (elem \ "name").text
		  val cost = (elem \ "manacost").text
		  val (supertypes, types, subtypes) = parseTypes((elem \ "type").text)
		  val text = (elem \ "text").text

		  val (power, toughness) = parsePT((elem \ "pt").text)

		  Card(	ManaParser.parseAll(ManaParser.cost, cost).get,
		      	name,
		      	types, supertypes, subtypes,
		      	text,
		      	power,
		      	toughness )
		}
	  else throw new Exception(name + " not found")
	}
}