package ebarrientos.deckStats

import ebarrientos.deckStats.stringParsing.ManaParser
import org.scalatest.FlatSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.White
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.HybridMana
import ebarrientos.deckStats.basics.ColorlessMana
import ebarrientos.deckStats.basics.Blue
import ebarrientos.deckStats.basics.Mana
import ebarrientos.deckStats.basics.ColorlessMana

@RunWith(classOf[JUnitRunner])
class ManaParseTest extends FlatSpec {

  "ManaParser" should "parse mana costs as expected" in {
    val costStr = "2WW(2/U)"

    val expected = List(
        ColorlessMana(2),
        ColoredMana(White),
        ColoredMana(White),
        HybridMana(Set(ColorlessMana(2), ColoredMana(Blue))))
        .sortWith(CardTestUtils.manaSort)

    val res = ManaParser.parseAll(ManaParser.cost, costStr).get.sortWith(CardTestUtils.manaSort)

    assert(expected === res)
  }
}