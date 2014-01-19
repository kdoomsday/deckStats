package ebarrientos.deckStats

import ebarrientos.deckStats.basics.Mana

object CardTestUtils {

  /** Function that determines whether one mana is "less than" another. This is useful as a sorting
    * function so that lists of mana can be compared usefully.
    */
  def manaSort = (mana1: Mana, mana2: Mana) => mana1.toString < mana2.toString
}