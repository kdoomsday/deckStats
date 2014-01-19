package ebarrientos.deckStats.math
import ebarrientos.deckStats.Deck

object Calc {

  def avgManaCost(d: Deck): Double =
    (d.cards.foldLeft(0.0)((pre, x) => pre + x.cmc).asInstanceOf[Double] / d.cards.length)
}