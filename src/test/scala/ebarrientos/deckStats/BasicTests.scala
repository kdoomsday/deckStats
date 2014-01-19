package ebarrientos.deckStats

import basics.White
import ebarrientos.deckStats.basics.Mana
import ebarrientos.deckStats.basics.HybridMana
import ebarrientos.deckStats.basics.ColorlessMana
import ebarrientos.deckStats.basics.ColoredMana
import ebarrientos.deckStats.basics.Creature
import ebarrientos.deckStats.basics.Instant
import ebarrientos.deckStats.basics.Land
import basics.Black
import basics.Green
import basics.Blue
import basics.White
import basics.Red
import ebarrientos.deckStats.math.Calc

object BasicTests extends App {
  
  manaTests
  cardTests
  deckTests

  // *** CMC tests
  def assert(title: String, b: =>Boolean) = {
    if (!b) throw new Exception("""Failed test: """" + title + """"""")
  }
  
  
  def manaTests = {
    var m: Mana = ColorlessMana(3)
	  assert("Colorless cmc", m.cmc == 3)
	  
	  m = ColoredMana(White)
	  assert("Colored cmc", m.cmc == 1)
	  
	  m = HybridMana(Set(ColoredMana(White), ColorlessMana(2)))
	  assert("Hybrid mana cmc", m.cmc == 2)
	  
	  try {
	    m = ColoredMana(Black)
	    assert("Force cmc error", m.cmc==2)
	    println("Assertion error")
	    System.exit(1)
	  }
	  catch {
	    case _ =>	// Should have an exception. If not app dies before
	  }
	  
	  
	  // *** Color tests
	  m = ColoredMana(Green)
	  assert("Check correct color", m.is(Green))
	  assert("Check it's not wrong color", !m.is(Blue))
	  assert("Colored mana is not colorless", !m.isColorless)
	  
	  m = ColorlessMana(5)
	  assert("Colorless is not of a color", !m.is(White))
	  assert("Colorless is colorless", m.isColorless)
	  
	  m = HybridMana(Set(ColoredMana(Red), ColorlessMana(1)))
	  assert("Hybrid is of the correct color", m.is(Red))
	  assert("Hybrid is not the wrong color", !m.is(Black))
	  assert("Hybrid with color is not colorless", !m.isColorless)
	  
	  println("All mana tests passed")
  }
  
  def cardTests = {
    val w = ColoredMana(White)
    val b = ColoredMana(Black)
    val dos = ColorlessMana(2)
    val hyb = HybridMana(Set(w, b))
    val hyb2 = HybridMana(Set(w, dos))
    
    var c = Card(IndexedSeq(w), "nombre", Set(Creature))
    assert("CMC w/ single colored mana", c.cmc == 1)
    
    c = Card(IndexedSeq(w, b), "nombre", Set(Creature))
    assert("CMC w/ two colored mana", c.cmc == 2)
    
    c = Card(IndexedSeq(dos), "name", Set(Creature))
    assert("CMC w/ two colorless", c.cmc == 2)
    
    c = Card(IndexedSeq(w, dos), "name", Set(Creature))
    assert("CMC w/ one colored and two colorless", c.cmc == 3)
    
    c = Card(IndexedSeq(hyb), "name", Set(Creature))
    assert("CMC w/ two color hybrid", c.cmc == 1)
    
    c = Card(IndexedSeq(hyb2), "name", Set(Creature))
    assert("CMC w/ colored and colorless hybrid", c.cmc == 2)
    
    println("All card tests passed")
  }
  
  
  def deckTests = {
    val stripMine = Card(IndexedSeq(), "Strip Mine", Set(Land))
    val stp = Card(IndexedSeq(ColoredMana(White)), "Swords to Plowshares", Set(Instant))
    val dc = Card(IndexedSeq(ColorlessMana(1), ColoredMana(Black)), "Dark Confidant", Set(Creature))
    
    val deck = Deck(IndexedSeq(stripMine, stp, dc))
    
    assert("Avg cmc of a deck", Calc.avgManaCost(deck) == 1.0)
    
    println("All deck tests passed")
  }
}