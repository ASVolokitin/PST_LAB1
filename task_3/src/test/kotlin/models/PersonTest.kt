package models

import com.sashka.enums.PersonState
import com.sashka.models.Dimensions
import com.sashka.models.Location
import com.sashka.models.Person
import com.sashka.states.MoodState
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows

class PersonTest {

    private lateinit var person: Person
    private val defaultLocation = Location(0.0, 0.0)
    private val defaultDimensions = Dimensions(0.5, 0.5, 1.8)

    @BeforeEach
    fun setUp() {
        person = Person(PersonState.STANDING, "Oluh", 80.0, defaultLocation, defaultDimensions, MoodState.CALM)
    }

    @Test
    fun shouldCreatePersonWithNameConstructor() {
        val namedPerson = Person("Oluh")

        assertEquals(PersonState.STANDING, namedPerson.currentState, "Person should be standing by default")
        assertEquals("Oluh", namedPerson.displayName, "Display name should be set correctly")
        assertEquals(80.0, namedPerson.weight, "Weight should be default 80.0")
        assertEquals(defaultLocation, namedPerson.currentLocation, "Location should be default")
        assertEquals(defaultDimensions, namedPerson.dimensions, "Dimensions should be default")
        assertEquals(MoodState.CALM, namedPerson.mood, "Mood should be CALM by default")
    }

    @Test
    fun shouldChangeMood() {
        person.changeMood(MoodState.GRUMPY)

        assertEquals(MoodState.GRUMPY, person.mood, "Mood should change to GRUMPY")
    }

    @Test
    fun shouldThrowExceptionWhenChangingToSameMood() {
        val exception = assertThrows<IllegalArgumentException> {
            person.changeMood(MoodState.CALM)
        }

        assertEquals(exception.message, "${person.displayName} is already ${MoodState.CALM}", "Should throw exception when changing to same mood")
    }

    @Test
    fun shouldThrowExceptionWithCustomMessageWhenChangingToSameMood() {
        val customMessage = "Custom mood error message"

        val exception = assertThrows<IllegalArgumentException> {
            person.changeMood(MoodState.CALM, customMessage)
        }

        assertEquals(customMessage, exception.message, "Should use custom error message")
    }
}