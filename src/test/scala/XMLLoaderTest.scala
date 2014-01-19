import ebarrientos.deckStats.load.XMLCardLoader
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import ebarrientos.deckStats.basics.Legendary
import ebarrientos.deckStats.basics.Creature
import org.scalatest.BeforeAndAfterAll
import ebarrientos.deckStats.basics.White
import ebarrientos.deckStats.basics.Black
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.ColorlessMana
import ebarrientos.deckStats.CardTestUtils
import ebarrientos.deckStats.basics.Sorcery
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.ColorlessMana
import ebarrientos.deckStats.basics.Supertype

@RunWith(classOf[JUnitRunner])
class XMLLoaderTest extends FlatSpec with BeforeAndAfterAll {
  lazy val loader = new XMLCardLoader(getClass().getResource("cards.xml").getFile())

  "Loader" should "load a creature correctly" in {
    val radiant = loader.card("Radiant, Archangel")

    assert(radiant.name === "Radiant, Archangel")
    assert(radiant.cmc === 5)
    assert(radiant.supertypes === Set(Legendary))
    assert(radiant.types === Set(Creature))
    assert(radiant.subtypes === Set("Angel"))
    assert(radiant.power === 3)
    assert(radiant.toughness === 3)
    assert(radiant is White)
    assert(!radiant.is(Black))

    val cost = Seq(ColoredMana(White), ColoredMana(White), ColorlessMana(3))
    assert(radiant.cost.sortWith(CardTestUtils.manaSort) === cost.sortWith(CardTestUtils.manaSort))
  }

  it should "load a non-creature correctly" in {
    val dt = loader.card("Demonic Tutor")

    assert(dt.name === "Demonic Tutor")
    assert(dt.cmc === 2)
    assert(dt.types === Set(Sorcery))
    assert(dt.supertypes.isEmpty)
    assert(dt.subtypes.isEmpty)
    assert(dt is Black)
    assert(!dt.is(White))
    assert(dt.power === 0)
    assert(dt.toughness === 0)

    val cost = Seq(ColoredMana(Black), ColorlessMana(1))
    assert(dt.cost.sortWith(CardTestUtils.manaSort) === cost.sortWith(CardTestUtils.manaSort))
  }
}