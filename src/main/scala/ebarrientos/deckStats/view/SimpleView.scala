package ebarrientos.deckStats.view

import scala.swing._
import scala.swing.event.ButtonClicked
import java.awt.Dimension

/** Main interface that shows a selector for the card database, a selector for the deck, and an
  * area for showing the deck stats.
  */
object SimpleView extends SimpleSwingApplication {

  def top = new MainFrame {
    title = "Simple deck stats view"
    size = new Dimension(200, 300)

    val textArea = new TextArea

    contents = new BorderPanel {
      layout(selectorPanel(this)) = BorderPanel.Position.North
      layout(textArea) = BorderPanel.Position.Center
    }

    centerOnScreen()
  }

  // Full contents of the area that will be used for selecting card base and deck
  private[this] def selectorPanel(parent: Component) = {
    val labelCards = new Label("Cards File:")
    val pathCards = new TextField
    val buttonChooseCards = new Button("...")
    val chooserCards = new FileChooser

    val labelDeck  = new Label("Deck File:")
    val pathDeck = new TextField
    val buttonChooseDeck = new Button("...")
    val chooserDeck = new FileChooser


    val panel = new GridPanel(2, 3) {
      contents += labelCards
      contents += pathCards
      contents += buttonChooseCards

      contents += labelDeck
      contents += pathDeck
      contents += buttonChooseDeck

      listenTo(buttonChooseCards, buttonChooseDeck)
      reactions += {
        case ButtonClicked(`buttonChooseCards`) =>
          if (chooserCards.showOpenDialog(parent) == FileChooser.Result.Approve)
            pathCards.text = chooserCards.selectedFile.getAbsolutePath()

        case ButtonClicked(`buttonChooseDeck`) =>
          if (chooserDeck.showOpenDialog(parent) == FileChooser.Result.Approve)
            pathDeck.text = chooserDeck.selectedFile.getAbsolutePath()
      }
    }

    panel
  }
}