package ebarrientos.deckStats

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import ebarrientos.deckStats.basics.Card
import ebarrientos.deckStats.basics.Land
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.White
import ebarrientos.deckStats.basics.ColorlessMana
import ebarrientos.deckStats.basics.Instant
import ebarrientos.deckStats.basics.Creature
import ebarrientos.deckStats.basics.Black
import ebarrientos.deckStats.basics.Deck
import ebarrientos.deckStats.math.Calc

@RunWith(classOf[JUnitRunner])
class CalcTests extends FlatSpec {

  "Calc" should "calculate values of a deck correctly" in {
    val stripMine = Card(IndexedSeq(), "Strip Mine", Set(Land))
    val stp = Card(IndexedSeq(ColoredMana(White)), "Swords to Plowshares", Set(Instant))
    val dc = Card(IndexedSeq(ColorlessMana(1), ColoredMana(Black)), "Dark Confidant", Set(Creature))

    val deck = Deck(IndexedSeq(stripMine, stp, dc))

    assert(Calc.avgManaCost(deck) == 1.0)
  }
}