package commands

import com.sashka.commands.WalkCommand
import com.sashka.models.Person
import com.sashka.enums.PersonState
import com.sashka.models.Location
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows

class WalkCommandTest {

    private lateinit var person: Person
    private lateinit var startLocation: Location
    private lateinit var destination: Location
    private lateinit var command: WalkCommand

    @BeforeEach
    fun setUp() {
        startLocation = Location(0.0, 0.0)
        destination = Location(10.0, 10.0)
        person = Person(PersonState.STANDING,"Duraley", initialLocation = startLocation)
        command = WalkCommand(person, destination)
    }

    @Test
    fun shouldWalkToDestination() {
        command.execute()

        assertEquals(PersonState.STANDING, person.currentState, "${person.displayName} should be standing after walk")
        assertEquals(destination, person.currentLocation, "${person.displayName} should be at destination")
    }
}