package ebarrientos.deckStats.parsing

import ebarrientos.deckStats.stringParsing.ManaParser
import org.scalatest.FlatSpec
import org.junit.runner.RunWith
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.White
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.HybridMana
import ebarrientos.deckStats.basics.ColorlessMana
import ebarrientos.deckStats.basics.Blue
import ebarrientos.deckStats.basics.ColorlessMana
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.CardTestUtils.manaSort
import ebarrientos.deckStats.basics.XMana
import org.scalatest.junit.JUnitRunner
import ebarrientos.deckStats.basics.ColoredMana

@RunWith(classOf[JUnitRunner])
class ManaParseTest extends FlatSpec {

  "ManaParser" should "parse mana costs as expected" in {
    val costStr = "2WW(2/U)"

    val expected = List(
        ColorlessMana(2),
        ColoredMana(White),
        ColoredMana(White),
        HybridMana(Set(ColorlessMana(2), ColoredMana(Blue))))
        .sortWith(manaSort)

    val res = ManaParser.parseAll(ManaParser.cost, costStr).get.sortWith(manaSort)

    assert(expected === res)
  }


  it should "parse X costs correctly" in {
    val costStr = "XW"

    val expected = List(XMana(), ColoredMana(White)).sortWith(manaSort)
    val res = ManaParser.parseAll(ManaParser.cost, costStr).get.sortWith(manaSort)

    assert(expected === res)
  }

  it should "parse empty mana as empty seq" in {
    val expected = Seq()
    val res = ManaParser.parseAll(ManaParser.cost, "").get
    assert(expected === res)
  }

  it should "parse phyrexian mana" in {
    val expected = Seq(ColoredMana(White))
    val res = ManaParser.parseAll(ManaParser.cost, "(W/P)").get
    assert(expected === res)
  }
}