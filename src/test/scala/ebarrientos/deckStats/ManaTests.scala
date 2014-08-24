package ebarrientos.deckStats

import ebarrientos.deckStats.basics.{Black, Blue, ColoredMana, ColorlessMana, Green, HybridMana, Red, White}
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

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