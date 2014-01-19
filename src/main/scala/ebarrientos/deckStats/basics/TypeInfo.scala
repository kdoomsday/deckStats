package ebarrientos.deckStats.basics

/**
 * Possible card types.
 */
sealed abstract class CardType
object CardType {
  private[this] val types =
    IndexedSeq(Creature, Artifact, Land, Instant, Sorcery, Tribal, Enchantment, Planeswalker)
    
  // Convert a string into a type
  def apply(s: String): CardType = {
    types.foreach(x => if (s == x.toString()) return x)
    throw new Exception("Unknown card type")
  }
  
  def isType(s: String) = types.foldLeft(false)((p, n) => p || s == n.toString)
}

case object Creature extends CardType
case object Artifact extends CardType
case object Land extends CardType
case object Instant extends CardType
case object Sorcery extends CardType
case object Tribal extends CardType
case object Enchantment extends CardType
case object Planeswalker extends CardType


/**
 * Possible card supertypes
 */
sealed abstract class Supertype
object Supertype {
  private[this] val supertypes = IndexedSeq(Legendary, Basic)
  
  def apply(s: String): Supertype = {
    supertypes.foreach(x => if (s == x.toString) return x)
    throw new Exception("Unknown supertype")
  }
  
  def isSupertype(s: String) = supertypes.foldLeft(false)((p, n) => p || s == n.toString())
}

case object Legendary extends Supertype
case object Basic extends Supertype