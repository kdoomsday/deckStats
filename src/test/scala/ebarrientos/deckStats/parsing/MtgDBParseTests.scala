package ebarrientos.deckStats.parsing

import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import ebarrientos.deckStats.basics.ColorlessMana
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.Black
import ebarrientos.deckStats.CardTestUtils
import ebarrientos.deckStats.basics.XMana
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.Green
import ebarrientos.deckStats.basics.Mana
import ebarrientos.deckStats.basics.ColorlessMana
import ebarrientos.deckStats.basics.HybridMana
import ebarrientos.deckStats.basics.ColorlessMana
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.White
import ebarrientos.deckStats.basics.HybridMana
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.ColoredMana

@RunWith(classOf[JUnitRunner])
class MtgDBParseTests extends FlatSpec {

  "MtgDBParseTests" should "parse regular costs" in {
    check("1B", List(ColorlessMana(1), ColoredMana(Black)))
  }

  it should "parse X mana" in {
    check("XG", List(XMana(), ColoredMana(Green)))
  }

  it should "parse colorless mana" in {
    check("16", List(ColorlessMana(16)))
  }

  it should "parse hybrid mana" in {
    check(
        "{2/W}{2/W}{2/W}",
        List(
            HybridMana( Set(ColorlessMana(2), ColoredMana(White)) ),
            HybridMana( Set(ColorlessMana(2), ColoredMana(White)) ),
            HybridMana( Set(ColorlessMana(2), ColoredMana(White)) )
        )
    )
  }

  it should "also parse hybrid mana" in {
    check ("{B/G}", List(HybridMana(Set(ColoredMana(Black), ColoredMana(Green)))))
  }


  it should "parse phyrexian mana" in {
    check("{WP}", List(ColoredMana(White)))
  }


  private[this] def check(in: String, exp: List[Mana]) = {
    import CardTestUtils.manaSort
    import _root_.ebarrientos.deckStats.stringParsing.MtgDBManaParser.{parseAll, cost}

    val expected = exp.sortWith(manaSort)
    val res = parseAll(cost, in).get.sortWith(manaSort)
    assert(expected === res)
  }
}