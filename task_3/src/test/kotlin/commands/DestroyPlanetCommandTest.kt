package commands

import com.sashka.commands.DestroyPlanetCommand
import com.sashka.enums.CharacterNames
import com.sashka.models.Dimensions
import com.sashka.models.Person
import com.sashka.models.Planet
import com.sashka.states.MoodState
import com.sashka.states.PlanetState
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DestroyPlanetCommandTest {

    private lateinit var planet: Planet

    @BeforeEach
    fun setUp() {
        planet = Planet(PlanetState.INHABITED, Dimensions(4000.0, 4000.0, 4000.0), "Sharik")
    }

    @Test
    fun testPlanetShouldBeDestroyed() {
        val person = Person("Balabol")
        val destroyPlanetCmd = DestroyPlanetCommand(person, planet)
        destroyPlanetCmd()
        assert(planet.currentState == PlanetState.DESTROYED)
    }

    @Test
    fun testJeltzShouldBecomeGrumpyAfterPlanetDistruction() {
        val person = Person(CharacterNames.JELTZ.name)
        assert(person.mood == MoodState.CALM)
        val destroyPlanetCmd = DestroyPlanetCommand(person, planet)
        destroyPlanetCmd()
        assert(planet.currentState == PlanetState.DESTROYED)
        assert(person.mood == MoodState.GRUMPY)
    }
}