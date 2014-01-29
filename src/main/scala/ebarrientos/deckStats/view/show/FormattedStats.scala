package ebarrientos.deckStats.view.show

import scala.swing.GridPanel
import scala.swing.BoxPanel
import scala.swing.Orientation
import scala.swing.Label
import scala.swing.TextArea
import ebarrientos.deckStats.basics.Deck
import scala.swing.Component
import ebarrientos.deckStats.math.Calc
import ebarrientos.deckStats.basics.Land
import ebarrientos.deckStats.basics.Instant
import ebarrientos.deckStats.basics.Sorcery
import ebarrientos.deckStats.basics.Creature
import java.awt.Font
import scala.swing.Alignment

class FormattedStats extends ShowStats {
  lazy val component = buildComponent()

  private[this] lazy val avgCostLabel = new Label("")
  private[this] lazy val avgNonlandCostLabel = new Label("")
  private[this] lazy val landCountLabel = new Label("")
  private[this] lazy val creatureCountLabel = new Label("")
  private[this] lazy val instSorcCountLabel = new Label("")
  private[this] lazy val otherCountLabel = new Label("")

  lazy val curveArea = new TextArea
  curveArea.editable = false
  curveArea.font = new Font(Font.MONOSPACED, Font.TRUETYPE_FONT, 12)

  private[this] val doubleFormat = "%.02f"


  def show(d: Deck) = {
    avgCostLabel.text = doubleFormat format Calc.avgManaCost(d)
    avgNonlandCostLabel.text = doubleFormat format Calc.avgManaCost(d, !_.is(Land))

    landCountLabel.text = Calc.count(d, _.is(Land)).toString
    creatureCountLabel.text = Calc.count(d, c => c.is(Creature) && !c.is(Land)).toString
    instSorcCountLabel.text = Calc.count(d, c => c.is(Instant) || c.is(Sorcery)).toString
    otherCountLabel.text =
      Calc.count(d, c => !c.is(Land) && !c.is(Creature) && !c.is(Instant) && !c.is(Sorcery)).toString

    printManaCurve(d)

    component.revalidate()
  }


  // Arrange the component
  private[this] def buildComponent() = {
    // I will use grids for all of this. Later I may switch to a gridbag or something else

    //align(landCountLabel, creatureCountLabel, instSorcCountLabel, otherCountLabel)

    
    val averages = new GridPanel(2, 2)
    averages.contents += new Label("Avg. cmc:")
    averages.contents += avgCostLabel
    averages.contents += new Label("Avg. cmc nonlands:")
    averages.contents += avgNonlandCostLabel
    
    val sep = new scala.swing.Separator(Orientation.Horizontal)

    val counts = new GridPanel(4, 2)
    counts.contents += new Label("Lands:")
    counts.contents += landCountLabel
    counts.contents += new Label("Creatures:")
    counts.contents += creatureCountLabel
    counts.contents += new Label("Instants/Sorceries:")
    counts.contents += instSorcCountLabel
    counts.contents += new Label("Other:")
    counts.contents += otherCountLabel

    val info = new BoxPanel(Orientation.Vertical)
    info.contents += averages
    info.contents += sep
    info.contents += counts

    val panel = new GridPanel(1, 2)
    panel.contents += info
    panel.contents += curveArea
    panel
  }


  /** Print the mana curve into a text area. */
  private[this] def printManaCurve(d: Deck) = {
    curveArea.text = ""

    val encodings = Calc.manaCurve(d)

    for ((cost, amount) <- encodings) {
      curveArea.append("%2d: [%2d] ".format(cost, amount))
      curveArea.append("*" * amount)
      curveArea.append(System.getProperty("line.separator"))
    }
  }


  /** Make a horizontal panel with n components. */
  private[this] def hGrid(components: Component*) = {
    val panel = new GridPanel(1, components.size)
    for (comp <- components)
      panel.contents += comp
    panel
  }


  /** Helper to build a label with a specified alignment. */
  private[this] def label(text: String, alignment: Alignment.Value = Alignment.Right) = {
    val lab = new Label(text)
    lab.horizontalAlignment = alignment
    lab
  }



  /** Align labels right */
  private[this] def align(labels: Label*)(implicit alignment: Alignment.Value = Alignment.Right) =
    for (label <- labels)
      label.horizontalAlignment = alignment
}