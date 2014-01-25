package ebarrientos.deckStats.math

import ebarrientos.deckStats.basics.Deck
import ebarrientos.deckStats.basics.CardType
import ebarrientos.deckStats.basics.Card
import ebarrientos.deckStats.basics.Land

/** Provides operations to apply to decks of cards to get information. */
object Calc {
  // Average of a seq of numbers
  private[this] def avg(seq: Seq[Int]) =
    seq.foldLeft(0.0)(_ + _) / seq.size


  /** Average mana cost of the whole deck. */
  def avgManaCost(d: Deck): Double =
    avg(d.cards.map(_.cmc))


  /** Average cost of cards that fulfill a condition. */
  def avgManaCost(deck: Deck, pred: Card => Boolean) =
    avg(deck.cards.filter(pred).map(_.cmc))

  /** Total number of cards. */
  def count(deck: Deck) = deck.cards.size

  /** Count the number of cards in a deck that match a certain predicate. */
  def count(deck: Deck, pred: Card => Boolean) =
    deck.cards.filter(pred).size


  /** Show a mana curve for cards that match a criterion. By default gives manacurve of all nonland
    * cards. The curve is a sequence of (manacost, amount), starting at mana cost zero up to the
    * greatest cost in the deck. All non-appearing costs within this range are included as zero.
    */
  def manaCurve(d: Deck, criterion: Card => Boolean = !_.is(Land)): Seq[(Int, Int)] = {
    // Compare tuples only by the first element.
    def lt(a: Tuple2[Int, Int], b: Tuple2[Int, Int]) = a._1 < b._1

    /** "Fill in" values that are not present. Only fills until the max. Seems an awful way to do
      * this, so should improve later.
      */
    def fillmap(m: Map[Int, Int]): Map[Int, Int] = {
      val max = m.keys.max
      var res = m
      (0 to max) foreach { i => if (!res.contains(i)) res = res updated (i, 0)}
      res
    }

    val map = Seqs.encode(d.cards.filter(criterion) map (_.cmc))

    if (map.isEmpty) map.toSeq
    else fillmap(map).toSeq.sortWith(lt)
  }
}