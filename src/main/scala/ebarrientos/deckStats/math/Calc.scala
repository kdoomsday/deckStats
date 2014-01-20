package ebarrientos.deckStats.math

import ebarrientos.deckStats.basics.Deck
import ebarrientos.deckStats.basics.CardType
import ebarrientos.deckStats.basics.Card

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
}