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
 * Hybrid Mana implementation. Contains a set of options that describe it. It's a list of list
 * because payment might be one mana of a color or multiple colorless.
 */
case class HybridMana(options: Set[Mana]) extends Mana {
  def this(opts: List[Mana]) = this(opts.toSet)
  
  // It *is* of a color if at least one of the options is
  override def is(c: Color): Boolean = applyCrit(false, m => m.is(c))
  
  // It's colorless if all options are colorless. Can't happen yet, but might with properties
  override def isColorless: Boolean = applyCrit(true, (m => !m.isColorless))
  
  
  private[this] def getProperties = {
    var props = Set[ManaProperty]()
    for (m <- options) props = props ++ m.properties
    props
  }
  
  override lazy val properties = getProperties
  
  override def hasProperty(p: ManaProperty): Boolean =
    applyCrit(false, (m: Mana) => m.hasProperty(p))
    
    
  override def cmc = options.foldLeft(0)((x, y) => if (x > y.cmc) x else y.cmc)
    
  /**
   * Apply a criterion to all options.
   * @param default Default value
   * @return <code>!default</code> if criterion passes for some option. Otherwise, default
   */
  private[this] def applyCrit(default: Boolean, f: Mana => Boolean): Boolean = {
    options foreach (m => if (f(m)) return !default)
    default
  }
}