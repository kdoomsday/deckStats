package ebarrientos.deckStats.view.show

import ebarrientos.deckStats.basics.Deck
import scala.swing.TextArea
import ebarrientos.deckStats.math.Calc
import ebarrientos.deckStats.basics.Land
import ebarrientos.deckStats.basics.Creature

/** Shows stats about a deck as a textual representation. */
object TextAreaStats extends ShowStats {

  def show(d: Deck) = {
    val res = new TextArea

    res.append(
      s"""Avg mana cost: ${Calc.avgManaCost(d)}
      |Avg nonland cost: ${Calc.avgManaCost(d, !_.is(Land))}
      |-----
      |Lands: ${Calc.count(d, _.is(Land))}
      |Creatures: ${Calc.count(d, _.is(Creature))}
      |Other: ${Calc.count(d, c => !c.is(Creature) && !c.is(Land))}""".stripMargin
    )

    res
  }
}