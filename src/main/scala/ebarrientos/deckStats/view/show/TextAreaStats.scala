package ebarrientos.deckStats.view.show

import ebarrientos.deckStats.basics.Deck
import scala.swing.TextArea
import ebarrientos.deckStats.math.Calc
import ebarrientos.deckStats.math.Seqs.encode
import ebarrientos.deckStats.basics.Land
import ebarrientos.deckStats.basics.Creature
import java.awt.Font

/** Shows stats about a deck as a textual representation. */
object TextAreaStats extends ShowStats {
  private[this] val linesep = System.getProperty("line.separator")

  def show(d: Deck) = {
    val res = new TextArea
    res.font = new Font(Font.MONOSPACED, Font.TRUETYPE_FONT, 12)

    res.append(
      s"""Avg mana cost: ${Calc.avgManaCost(d)}
      |Avg nonland cost: ${Calc.avgManaCost(d, !_.is(Land))}
      |-----
      |Lands: ${Calc.count(d, _.is(Land))}
      |Creatures: ${Calc.count(d, _.is(Creature))}
      |Other: ${Calc.count(d, c => !c.is(Creature) && !c.is(Land))}
      |-----
      |""".stripMargin
    )

    printManaCurve(d, res)

    res
  }

  /** Print the manacurve into a text area. */
  private[this] def printManaCurve(d: Deck, t: TextArea) = {
    def lt(a: Tuple2[Int, Int], b: Tuple2[Int, Int]) = a._1 < b._1

    /** "Fill in" valueas that are not present. Only fills until the max. Seems an awful way to do
      * this, so should improve later.
      */
    def fillmap(m: Map[Int, Int]): Map[Int, Int] = {
      val max = m.keys.max
      var res = m
      (0 to max) foreach { i => if (!res.contains(i)) res = res updated (i, 0)}
      res
    }

    val map = encode(d.cards.filter(!_.is(Land)) map (_.cmc))
    val encodings = fillmap(map).toList.sortWith(lt)

    for ((cost, amount) <- encodings) {
      t.append("%2d: [%2d] ".format(cost, amount))
      t.append("*" * amount)
      t.append(linesep)
    }
  }
}