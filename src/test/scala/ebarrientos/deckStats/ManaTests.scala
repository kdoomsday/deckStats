package ebarrientos.deckStats

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._
import ebarrientos.deckStats.basics.Mana
import ebarrientos.deckStats.basics.White
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.HybridMana
import ebarrientos.deckStats.basics.Black
import ebarrientos.deckStats.basics.ColorlessMana
import org.scalatest.matchers.ClassicMatchers
import org.scalatest.matchers.ShouldMatchers
import ebarrientos.deckStats.basics.Green
import ebarrientos.deckStats.basics.Blue
import ebarrientos.deckStats.basics.Red

@RunWith(classOf[JUnitRunner])
class ManaTests extends FlatSpec {

  "Mana" should "have correct mana costs" in {
    assert(ColoredMana(White).cmc === 1)
    assert(ColorlessMana(3).cmc === 3)

    val m = HybridMana(Set(ColoredMana(White), ColorlessMana(2)))
    assert(m.cmc === 2)
  }


  it should "evaluate colors correctly" in {
    val green = ColoredMana(Green)
    assert(green.is(Green))
    assert(!green.is(Blue))
    assert(!green.isColorless)

    val colorless = ColorlessMana(5)
    assert(!colorless.is(White))
    assert(colorless.isColorless)

    val hybrid = HybridMana(Set(ColoredMana(Red), ColorlessMana(1)))
    assert(hybrid.is(Red))
    assert(!hybrid.is(Black))
    assert(!hybrid.isColorless)
  }
}