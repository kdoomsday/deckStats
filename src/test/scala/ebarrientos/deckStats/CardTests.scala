package ebarrientos.deckStats

import org.scalatest.FlatSpec
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.White
import ebarrientos.deckStats.basics.Black
import ebarrientos.deckStats.basics.ColorlessMana
import ebarrientos.deckStats.basics.HybridMana
import ebarrientos.deckStats.basics.Card
import ebarrientos.deckStats.basics.Creature
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CardTests extends FlatSpec {

  "Card" should "calculate mana related things correctly" in {
    val w = ColoredMana(White)
    val b = ColoredMana(Black)
    val dos = ColorlessMana(2)
    val hyb = HybridMana(Set(w, b))
    val hyb2 = HybridMana(Set(w, dos))

    assert(Card(IndexedSeq(w), "name", Set(Creature)).cmc === 1)
    assert(Card(IndexedSeq(w, b), "nombre", Set(Creature)).cmc === 2)
    assert(Card(IndexedSeq(dos), "name", Set(Creature)).cmc === 2)
    assert(Card(IndexedSeq(w, dos), "name", Set(Creature)).cmc === 3)
    assert(Card(IndexedSeq(hyb), "name", Set(Creature)).cmc === 1)
    assert(Card(IndexedSeq(hyb2), "name", Set(Creature)).cmc === 2)
  }
}