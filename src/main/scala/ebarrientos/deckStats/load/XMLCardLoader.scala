package ebarrientos.deckStats.load
import ebarrientos.deckStats.basics.CardType
import ebarrientos.deckStats.basics.Supertype
import ebarrientos.deckStats.stringParsing.ManaParser
import ebarrientos.deckStats.basics.Card

class XMLCardLoader(xmlFile: String) {
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

		  Card(	ManaParser.parseAll(ManaParser.cost, cost).get.toIndexedSeq,
		      	name,
		      	types, supertypes, subtypes,
		      	text,
		      	power,
		      	toughness )
		}
	  else throw new Exception(name + " not found")
	}

	/**
	 * Get a list of supertypes, types and subtypes
	 */
	private[this] def parseTypes(typeLine: String):
		(Set[Supertype], Set[CardType], Set[String]) =
	{
	  val ind = typeLine.indexOf("-")
	  val (types, subtypes) = if (ind > -1) (typeLine take ind, typeLine drop (ind+1))
	  												else (typeLine, "")

	  var restypes = IndexedSeq[CardType]()
	  var ressuper = IndexedSeq[Supertype]()

	  types.split(" ").foreach(x => {
	    if (CardType.isType(x)) restypes = restypes :+ CardType(x)
	    else if (Supertype.isSupertype(x)) ressuper = ressuper :+ Supertype(x)
	  })

	  // Be careful not to create a Set with an empty string instead of empty set
	  val subtypeset = if (subtypes == "") Set.empty[String]
	                   else subtypes.trim.split(" ").toSet

	  (ressuper.toSet, restypes.toSet, subtypeset)
	}


	// Parse a power / toughness string into values
	private[this] def parsePT(pt: String) = {
	  if (pt == "") (0, 0)
	  else {
  	  val vals = pt.split("/")
  	  (strToInt(vals.head), strToInt(vals.tail.head))
	  }
	}

	// Convert power or toughness to Int, taking into consideration things like '*'
  private[this] def strToInt(s: String) = {
    if (s.contains("*") || s.contains("X")) 0
    else if (s.contains("+")) s.takeWhile(_ != '+').toInt
    else s.toInt
  }
}