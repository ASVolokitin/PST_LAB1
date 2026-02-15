package models

import com.sashka.states.BuildingState
import com.sashka.enums.PersonState
import com.sashka.models.Bridge
import com.sashka.models.Dimensions
import com.sashka.models.Location
import com.sashka.models.Person
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows

class BridgeTest {

    private lateinit var bridge: Bridge
    private lateinit var person: Person
    private lateinit var startLocation: Location
    private lateinit var otherSideLocation: Location

    @BeforeEach
    fun setUp() {
        bridge = Bridge(BuildingState.BUILT, "TestBridge", Location(0.0, 0.0), Dimensions(10.0, 2.0, 3.0))
        startLocation = Location(0.0, 0.0)
        otherSideLocation = Location(startLocation.x + bridge.dimensions.length, startLocation.z + bridge.dimensions.width)
        person = Person(PersonState.STANDING, "Prostofilya", initialLocation = startLocation)
        person.currentState = PersonState.STANDING
    }

    @Test
    fun shouldCrossBridgeFromStartToOtherSide() {
        bridge.crossBridge(person)

        assertEquals(PersonState.STANDING, person.currentState, "${person.displayName} should be standing after crossing")
        assertEquals(otherSideLocation, person.currentLocation, "${person.displayName} should be on the other side of the bridge")
    }

    @Test
    fun shouldCrossBridgeFromOtherSideToStart() {
        person.currentLocation = otherSideLocation
        bridge.crossBridge(person)

        assertEquals(PersonState.STANDING, person.currentState, "${person.displayName} should be standing after crossing")
        assertEquals(startLocation, person.currentLocation, "${person.displayName} should be back at start")
    }

    @Test
    fun shouldThrowExceptionWhenPersonIsNotNearBridge() {
        person.currentLocation = Location(100.0, 100.0)

        val exception = assertThrows<IllegalStateException> {
            bridge.crossBridge(person)
        }

        assertEquals(exception.message, "${person.displayName} is not near ${bridge.displayName}", "Should throw exception when person is not near bridge")
    }
}