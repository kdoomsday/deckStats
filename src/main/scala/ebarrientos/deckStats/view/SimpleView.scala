package ebarrientos.deckStats.view

import scala.swing._
import scala.swing.event.ButtonClicked
import java.awt.Dimension

/** Main interface that shows a selector for the card database, a selector for the deck, and an
  * area for showing the deck stats.
  */
object SimpleView extends SimpleSwingApplication {
  lazy val pathDeck = new TextField
  lazy val pathCards = new TextField

  lazy val prefSize = new Dimension(300, 400)

  def top = new MainFrame {
    title = "Simple deck stats view"
    size = prefSize
    preferredSize = prefSize

    val textArea = new TextArea

    contents = new BorderPanel {
      layout(selectorPanel(this)) = BorderPanel.Position.North
      layout(textArea) = BorderPanel.Position.Center
    }

    centerOnScreen()
  }

  // Full contents of the area that will be used for selecting card base and deck
  private[this] def selectorPanel(parent: Component) = {
    def mPanel(l: Label, f: TextField, b: Button) = new BorderPanel {
      import BorderPanel.Position._
      layout(l) = West
      layout(f) = Center
      layout(b) = East
    }

    val labelCards = new Label("Cards File:")
    val buttonChooseCards = new Button("...")
    val chooserCards = new FileChooser

    val labelDeck  = new Label("Deck File:")
    labelDeck.preferredSize = labelCards.preferredSize
    val buttonChooseDeck = new Button("...")
    val chooserDeck = new FileChooser


    val panel = new GridPanel(2, 1) {
      contents += mPanel(labelCards, pathCards, buttonChooseCards)
      contents += mPanel(labelDeck, pathDeck, buttonChooseDeck)

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