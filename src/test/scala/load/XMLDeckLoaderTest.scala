package load

import org.scalatest.FlatSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import ebarrientos.deckStats.load.XMLCardLoader
import ebarrientos.deckStats.load.XMLDeckLoader
import scala.xml.Elem
import ebarrientos.deckStats.math.Calc
import ebarrientos.deckStats.load.CardLoader
import ebarrientos.deckStats.basics.ColorlessMana
import ebarrientos.deckStats.basics.Card
import ebarrientos.deckStats.load.XMLDeckLoader
import ebarrientos.deckStats.basics.Creature

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
    val deck = deckLoader.load

    assert(Calc.avgManaCost(deck) === 2)
  }

  it should "load the deck correctly" in {
    val loader = new XMLCardLoader(getClass().getResource("/cards.xml").getFile())

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
    val deck = deckLoader.load

    assert(Calc.avgManaCost(deck) === 2)
    assert(Calc.avgManaCost(deck, _.is(Creature)) === 3)
  }
}