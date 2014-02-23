package load

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import ebarrientos.deckStats.load.utils.LoadUtils
import ebarrientos.deckStats.basics.Basic
import ebarrientos.deckStats.basics.Land

@RunWith(classOf[JUnitRunner])
class LoadUtilsTest extends FlatSpec {

  val lu = new AnyRef() with LoadUtils

  "LoadUtils" should "parse typelines properly" in {
    val tl = "Basic Land - Forest"
    val (supertypes, types, subtypes) = lu.parseTypes(tl)

    assert(supertypes === Set(Basic))
    assert(types === Set(Land))
    assert(subtypes === Set("Forest"))
  }


  it should "parse empty subtypes correctly" in {
    val tl = "Land"
    val (supertypes, types, subtypes) = lu.parseTypes(tl)

    assert(supertypes === Set())
    assert(types === Set(Land))
    assert(subtypes === Set())
    assert(subtypes.isEmpty)
  }
}