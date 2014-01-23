package ebarrientos.deckStats.view.show

import scala.swing.Component
import ebarrientos.deckStats.basics.Deck

/** Given a deck, produce a representation of relevant information about it. */
trait ShowStats {

  /** Given a deck, return a Component that can be added to a view, to show info. */
  def show(d: Deck): Component
}