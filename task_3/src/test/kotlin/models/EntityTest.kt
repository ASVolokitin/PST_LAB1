package models

import com.sashka.enums.PersonState
import com.sashka.models.Dimensions
import com.sashka.models.Entity
import com.sashka.models.Location
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows

class EntityTest {

    private lateinit var entity: Entity<PersonState>
    private lateinit var location: Location
    private lateinit var dimensions: Dimensions

    @BeforeEach
    fun setUp() {
        location = Location(0.0, 0.0)
        dimensions = Dimensions(1.0, 1.0, 1.0)
        entity = object : Entity<PersonState>(
            currentState = PersonState.STANDING,
            displayName = "TestEntity",
            currentLocation = location,
            dimensions = dimensions,
            weight = 10.0
        ) {}
    }

    @Test
    fun shouldTransitionToNewState() {
        val expectedState = PersonState.WALKING
        entity.transitionTo(expectedState)

        assertEquals(PersonState.WALKING, entity.currentState, "Entity should be in $expectedState state")
    }

    @Test
    fun shouldThrowExceptionWhenTransitioningToSameState() {
        val exception = assertThrows<IllegalArgumentException> {
            entity.transitionTo(PersonState.STANDING)
        }

        assertEquals(exception.message, "${entity.displayName} is already ${PersonState.STANDING}", "Should throw exception when transitioning to same state")
    }

    @Test
    fun shouldThrowExceptionWithCustomMessage() {
        val customMessage = "Custom error message"

        val exception = assertThrows<IllegalArgumentException> {
            entity.transitionTo(PersonState.STANDING, customMessage)
        }

        assertEquals(customMessage, exception.message, "Should use custom error message")
    }
}