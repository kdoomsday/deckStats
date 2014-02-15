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
import java.util.ResourceBundle

class FormattedStats extends ShowStats {
  private[this] lazy val text = ResourceBundle.getBundle("locale/formattedStats/text")

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
    curveArea.append("%n%s%n".format("-" * 20))
    printSymbols(d)

    component.revalidate()
  }


  // Arrange the component
  private[this] def buildComponent() = {
    // I will use grids for all of this. Later I may switch to a gridbag or something else

    //align(landCountLabel, creatureCountLabel, instSorcCountLabel, otherCountLabel)


    val averages = new GridPanel(2, 2)
    averages.contents += new Label(text.getString("avgCost.all.label"))
    averages.contents += avgCostLabel
    averages.contents += new Label(text.getString("avgCost.nonLands.label"))
    averages.contents += avgNonlandCostLabel

    val sep = new scala.swing.Separator(Orientation.Horizontal)

    val counts = new GridPanel(4, 2)
    counts.contents += new Label(text.getString("count.lands.label"))
    counts.contents += landCountLabel
    counts.contents += new Label(text.getString("count.creatures.label"))
    counts.contents += creatureCountLabel
    counts.contents += new Label(text.getString("count.instSoc.label"))
    counts.contents += instSorcCountLabel
    counts.contents += new Label(text.getString("count.other.label"))
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
      curveArea.append("%2d: [%2d] %s%n".format(cost, amount, "*" * amount))
    }
  }


  private[this] def printSymbols(d: Deck) = {
    val mapSymbols = Calc.manaSymbols(d)
    val list = mapSymbols.filter{ case (_, v) => v > 0 }.toList
    list.sortWith{ case ((_, v1), (_, v2)) => v1 < v2 }.foreach{ case (k, v) =>
        curveArea.append("(%2d): %s%n".format(v, k*v))
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