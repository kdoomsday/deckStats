package ebarrientos.deckStats.view

import scala.swing._
import scala.swing.event.ButtonClicked
import java.awt.Dimension
import ebarrientos.deckStats.view.show.ShowStats
import ebarrientos.deckStats.view.show.TextAreaStats
import ebarrientos.deckStats.load.CardLoader
import ebarrientos.deckStats.load.DeckLoader
import ebarrientos.deckStats.load.XMLCardLoader
import ebarrientos.deckStats.load.XMLDeckLoader
import ebarrientos.deckStats.load.XMLDeckLoader
import ebarrientos.deckStats.load.CachedLoader

/** Main interface that shows a selector for the card database, a selector for the deck, and an
  * area for showing the deck stats.
  */
object SimpleView extends SimpleSwingApplication {
  lazy val pathDeck = new TextField
  lazy val pathCards = new TextField
  lazy val status = new Label("")
  lazy val panel = new FlowPanel

  lazy val prefSize = new Dimension(300, 400)


  private[this] var cardLoader: Option[CardLoader] = None
  private[this] var deckLoader: Option[DeckLoader] = None
  // What will actually show the information
  lazy val shower: ShowStats = TextAreaStats


  def top = new MainFrame {
    title = "Simple deck stats view"
    size = prefSize
    preferredSize = prefSize

    contents = new BorderPanel {
      layout(selectorPanel(this)) = BorderPanel.Position.North
      layout(panel) = BorderPanel.Position.Center
      layout(status) = BorderPanel.Position.South
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
          if (chooserCards.showOpenDialog(parent) == FileChooser.Result.Approve) {
            pathCards.text = chooserCards.selectedFile.getAbsolutePath()
            changeCardLoader
          }

        case ButtonClicked(`buttonChooseDeck`) =>
          if (chooserDeck.showOpenDialog(parent) == FileChooser.Result.Approve) {
            pathDeck.text = chooserDeck.selectedFile.getAbsolutePath()
            changeDeck
          }
      }
    }

    panel
  }


  /** Execute a block and, in case of error, show in the status bar. */
  private[this] def actionOrError(block: => Unit) = {
    try { block }
    catch {
      case e: Throwable => status.text = s"Error: ${e.getMessage()}"
    }
  }


  // When the card database is changed
  private[this] def changeCardLoader = {
    actionOrError {
      cardLoader = Some(new CachedLoader(new XMLCardLoader(pathCards.text)))
      changeDeck
    }
  }

  // When the deck is changed
  private[this] def changeDeck = {
    cardLoader map { loader =>
      actionOrError {
        deckLoader = Some(new XMLDeckLoader(pathDeck.text, loader))
        calculate
      }
    }
  }


  /** Perform the action of loading the deck and showing the stats. */
  private[this] def calculate = {
    // Handle loading of cards database and such prop
    for (loader <- deckLoader) {
      panel.contents.clear()
      panel.contents += shower.show(loader.load)
      panel.revalidate
    }
  }
}