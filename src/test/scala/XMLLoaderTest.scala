import ebarrientos.deckStats.load.XMLCardLoader

object XMLLoaderTest extends App {
	val loader = new XMLCardLoader(getClass().getResource("cards.xml").getFile())
	val radiant = loader.card("Radiant, Archangel")
	
	printf("%s %s%n", radiant.name, radiant.cost)
	printf("%s %s - %s%n", radiant.supertypes, radiant.types, radiant.subtypes)
	printf("%s%n", radiant.text)
}