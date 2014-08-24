package ebarrientos.deckStats.load

import ebarrientos.deckStats.basics.Card

import scala.collection.mutable

/** Loader that caches values in memory for repeated use. */
class CachedLoader(private val l: CardLoader) extends CardLoader {
  private[this] val map = mutable.HashMap[String, Card]()


  def card(name: String): Card =
    if (map contains name) map(name)
    else {
      val card = l.card(name)
      map(name) = card
      card
    }
}