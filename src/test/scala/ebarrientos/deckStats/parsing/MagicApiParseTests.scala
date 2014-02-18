package ebarrientos.deckStats.parsing

import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import ebarrientos.deckStats.stringParsing.MagicApiManaParser
import ebarrientos.deckStats.basics.ColorlessMana
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.Black
import ebarrientos.deckStats.basics.XMana
import ebarrientos.deckStats.basics.Green
import ebarrientos.deckStats.basics.Mana
import ebarrientos.deckStats.basics.White
import ebarrientos.deckStats.basics.HybridMana

@RunWith(classOf[JUnitRunner])
class MagicApiParseTests extends FlatSpec {

  "MtgApiManaParser" should "parse regular costs" in {
    check("{1}{B}", List(ColorlessMana(1), ColoredMana(Black)))
  }

  it should "parse X mana" in {
    check("{X}{G}", List(XMana(), ColoredMana(Green)))
  }

  it should "parse colorless mana" in {
    check("{16}", List(ColorlessMana(16)))
  }

  it should "parse hybrid mana" in {
    check(
        "{Two or White}{Two or White}{Two or White}",
        List(
            HybridMana( Set(ColorlessMana(2), ColoredMana(White)) ),
            HybridMana( Set(ColorlessMana(2), ColoredMana(White)) ),
            HybridMana( Set(ColorlessMana(2), ColoredMana(White)) )
        )
    )
  }

  it should "also parse hybrid mana" in {
    check ("{Black or Green}", List(HybridMana(Set(ColoredMana(Black), ColoredMana(Green)))))
  }


  private[this] def check(in: String, exp: List[Mana]) = {
    import ebarrientos.deckStats.CardTestUtils.manaSort
    import MagicApiManaParser.{parseAll, cost}

    val expected = exp.sortWith(manaSort)
    val res = parseAll(cost, in).get.sortWith(manaSort)
    assert(expected === res)
  }
}