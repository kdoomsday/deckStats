package ebarrientos.deckStats.basics


sealed class ManaProperty
case object Phyrexian extends ManaProperty
case object Snow extends ManaProperty


// Common behavior for mana
trait Mana {
  def is(c: Color): Boolean
  def isColorless: Boolean

  def properties: Set[ManaProperty]
  def hasProperty(p: ManaProperty): Boolean = properties(p)

  def cmc: Int
}


/** Colorless Mana impl. has an amount because it's generally grouped */
case class ColorlessMana( override val cmc: Int = 1,
    					  					override val properties: Set[ManaProperty] = Set() )
extends Mana
{
  override def is(c: Color) = false
  override val isColorless = true
}


/** Each instance is one colored mana. */
case class ColoredMana(color: Color, override val properties: Set[ManaProperty] = Set())
extends Mana
{
  override def is(c: Color): Boolean = color == c
  override val isColorless: Boolean = false
  override val cmc = 1
}



/**
 * Hybrid Mana implementation. Contains a set of options that describe it.
 */
case class HybridMana(options: Set[Mana]) extends Mana {
  def this(opts: List[Mana]) = this(opts.toSet)

  // It *is* of a color if at least one of the options is
  override def is(c: Color): Boolean =
    options.exists(mana => mana.is(c))

  // It's colorless if all options are colorless. Can't happen yet, but might with properties
  override def isColorless: Boolean = options.forall(_.isColorless)


  /** All properties this hybrid mana has. It's the union of all properties in all mana symbols
    * that conform it.
    */
  override lazy val properties =
    options.foldLeft(Set[ManaProperty]())((rs, mana) => rs ++ mana.properties)

  override def hasProperty(p: ManaProperty): Boolean =
    options.exists(mana => mana.hasProperty(p))


  /** Converted mana cost. */
  override def cmc = options.map(_.cmc).max
}