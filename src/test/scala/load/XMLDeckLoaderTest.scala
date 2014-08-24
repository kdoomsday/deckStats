package load

import ebarrientos.deckStats.basics.{Card, ColorlessMana, Creature}
import ebarrientos.deckStats.load.{CardLoader, XMLCardLoader, XMLDeckLoader}
import ebarrientos.deckStats.math.Calc
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class XMLDeckLoaderTest extends FlatSpec {

  "XMLDeckLoader" should "load correctly using dummy card loader" in {
    val loader = new CardLoader() {
      def card(name: String) =
        Card(Seq(ColorlessMana(name.length())), name, Set.empty, Set.empty, Set.empty)
    }

    val deckXML =
      <cockatrice_deck version="1">
        <deckname>GWB Goodstuff</deckname>
        <comments></comments>
        <zone name="main">
          <card number="2" price="0" name="1"/>
          <card number="1" price="0" name="four"/>
        </zone>
      </cockatrice_deck>

    val deckLoader = XMLDeckLoader(deckXML, loader)
    val deck = deckLoader.load()

    assert(Calc.avgManaCost(deck) === 2)
  }

  it should "load the deck correctly" in {
    val loader = new XMLCardLoader(getClass.getResource("/cards.xml").getFile)

    val deckXML =
      <cockatrice_deck version="1">
        <deckname>GWB Goodstuff</deckname>
        <comments></comments>
        <zone name="main">
          <card number="2" price="0" name="Knight of the Reliquary"/>
          <card number="1" price="0" name="Strip Mine"/>
        </zone>
      </cockatrice_deck>

    val deckLoader = XMLDeckLoader(deckXML, loader)
    val deck = deckLoader.load()

    assert(Calc.avgManaCost(deck) === 2)
    assert(Calc.avgManaCost(deck, _.is(Creature)) === 3)
  }
}