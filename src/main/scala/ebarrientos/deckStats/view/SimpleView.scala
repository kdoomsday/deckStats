package ebarrientos.deckStats.view

import scala.swing._
import scala.swing.event.ButtonClicked
import java.awt.Dimension
import ebarrientos.deckStats.view.show.ShowStats
import ebarrientos.deckStats.load.CardLoader
import ebarrientos.deckStats.load.DeckLoader
import ebarrientos.deckStats.load.XMLCardLoader
import ebarrientos.deckStats.load.XMLDeckLoader
import ebarrientos.deckStats.load.XMLDeckLoader
import ebarrientos.deckStats.load.CachedLoader
import ebarrientos.deckStats.view.show.FormattedStats
import scala.concurrent.impl.Future
import java.awt.Cursor
import java.util.ResourceBundle
import java.util.Locale

/** Main interface that shows a selector for the card database, a selector for the deck, and an
  * area for showing the deck stats.
  */
object SimpleView extends SimpleSwingApplication {
  lazy val text = ResourceBundle.getBundle("locale/text")
  lazy val pathDeck = new TextField
  lazy val pathCards = new TextField
  lazy val status = new Label(text.getString("statusbar.default"))

  lazy val prefSize = new Dimension(800, 400)

  private[this] var mainPanel: Panel = null


  private[this] var cardLoader: Option[CardLoader] = None
  private[this] var deckLoader: Option[DeckLoader] = None
  // What will actually show the information
  lazy val shower: ShowStats = new FormattedStats


  def top = new MainFrame {
    title = text.getString("main.title")
    size = prefSize
    preferredSize = prefSize

    mainPanel = new BorderPanel {
      layout(selectorPanel(this)) = BorderPanel.Position.North
      layout(shower.component) = BorderPanel.Position.Center
      layout(status) = BorderPanel.Position.South
    }

    contents = mainPanel

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

    val labelCards = new Label(text.getString("cardsdb.label"))
    val buttonChooseCards = new Button(text.getString("cardsdb.buttonChoose"))
    val chooserCards = new FileChooser

    val labelDeck  = new Label(text.getString("deck.label"))
    labelDeck.preferredSize = labelCards.preferredSize
    val buttonChooseDeck = new Button(text.getString("deck.buttonChoose"))
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
        if (pathDeck.text != "") {
          deckLoader = Some(new XMLDeckLoader(pathDeck.text, loader))
          calculate
        }
      }
    }
  }


  /** Perform the action of loading the deck and showing the stats. */
  private[this] def calculate = {
    import java.awt.Cursor._
    import scala.concurrent.future
    import scala.concurrent.ExecutionContext.Implicits._

    val task = future {
      // Handle loading of cards database and such prop
      for (loader <- deckLoader) {
        status.text = text.getString("statusbar.loading")
        setCursor(WAIT_CURSOR)
        shower.show(loader.load)
      }
    }

    task onSuccess {
      case _ => Swing.onEDT {
        status.text = text.getString("statusbar.loaded")
        setCursor(getDefaultCursor())
      }
    }

    task onFailure {
      case e: Throwable => Swing.onEDT {
        e.printStackTrace()
        status.text = text.getString("statusbar.error") + e.getMessage()
        setCursor(getDefaultCursor())
      }
    }
  }


  // Cursor manipulation functions
  private[this] def setCursor(c: Cursor): Unit = mainPanel.cursor = c
  private[this] def setCursor(cType: Int): Unit = setCursor(Cursor.getPredefinedCursor(cType))
}