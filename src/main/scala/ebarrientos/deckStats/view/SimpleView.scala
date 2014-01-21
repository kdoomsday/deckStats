package ebarrientos.deckStats.view

import scala.swing._

/** Main interface that shows a selector for the card database, a selector for the deck, and an
  * area for showing the deck stats.
  */
class SimpleView extends SimpleSwingApplication {

  def top = new MainFrame {
    title = "Simple deck stats view"

  }
}