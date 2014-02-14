package ebarrientos.deckStats

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import ebarrientos.deckStats.basics.Card
import ebarrientos.deckStats.basics.Land
import ebarrientos.deckStats.basics.Instant
import ebarrientos.deckStats.basics.Creature
import ebarrientos.deckStats.basics.Sorcery
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.ColorlessMana
import ebarrientos.deckStats.basics.White
import ebarrientos.deckStats.basics.Black
import ebarrientos.deckStats.basics.Deck
import ebarrientos.deckStats.math.Calc

@RunWith(classOf[JUnitRunner])
class CalcTests extends FlatSpec {
  val stripMine = Card(IndexedSeq(), "Strip Mine", Set(Land))
  val stp = Card(IndexedSeq(ColoredMana(White)), "Swords to Plowshares", Set(Instant))
  val dc = Card(
      IndexedSeq(ColorlessMana(1),
      ColoredMana(Black)),
      "Dark Confidant",
      Set(Creature),
      subtypes = Set("Human", "Wizard") )

  val deck = Deck(IndexedSeq(stripMine, stp, dc))

  "Calc" should "calculate values of a deck correctly" in {

    // Averages
    assert(Calc.avgManaCost(deck) == 1.0)
    assert(Calc.avgManaCost(deck, _.is(Instant)) === 1.0)
    assert(Calc.avgManaCost(deck, _.is(Black)) === 2.0)
    assert(Calc.avgManaCost(deck, !_.is(White)) === 1.0)

    // Counts
    assert(Calc.count(deck) === 3)
    assert(Calc.count(deck, _.is(Instant)) === 1)
    assert(Calc.count(deck, _.is(Creature)) === 1)
    assert(Calc.count(deck, _.isSubType("Human")) === 1)

  }


  it should "evaluate mana curve correctly" in {
    // Default filter -> Non lands
    assert(Calc.manaCurve(deck) === Seq((0, 0), (1, 1), (2, 1)))

    // All cards
    assert(Calc.manaCurve(deck, _ => true) === Seq((0, 1), (1, 1), (2, 1)))

    // Only creatures
    assert(Calc.manaCurve(deck, _.is(Creature)) === Seq((0, 0), (1, 0), (2, 1)))

    // Only instants
    assert(Calc.manaCurve(deck, _.is(Instant)) === Seq((0, 0), (1, 1)))

    // Only sorceries
    assert(Calc.manaCurve(deck, _.is(Sorcery)) === Seq())
  }



  it should "count mana symbols" in {
    val map = Map("W" -> 1, "B" -> 1, "C" -> 1)
    assert(Calc.manaSymbols(deck) === map)
  }
}