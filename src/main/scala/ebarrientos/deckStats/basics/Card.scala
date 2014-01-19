package ebarrientos.deckStats.basics


/**
 * Representation of a card.
 * Note: all cards in this representation have a power and toughness, even if they're not creatures.
 * If queried, noncreatures will return zero for both these values.
 */
case class Card(	cost: IndexedSeq[Mana],
    							name: String,
    							types: Set[CardType],
    							supertypes: Set[Supertype] = Set(),
    							subtypes: Set[String] = Set(),
    							text: String = "",
    							power: Int = 0,
    							toughness: Int = 0	)
{
	def cmc = cost.foldLeft(0)(_ + _.cmc)
}