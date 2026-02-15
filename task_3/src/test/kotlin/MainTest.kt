package com.sashka

import com.sashka.commands.DestroyPlanetCommand
import com.sashka.commands.SitOnArmchairCommand
import com.sashka.commands.WalkCommand
import com.sashka.enums.CharacterNames
import com.sashka.enums.PersonState
import com.sashka.models.*
import com.sashka.states.ArmchairState
import com.sashka.states.BuildingState
import com.sashka.states.MoodState
import com.sashka.states.PlanetState
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows

class MainScenarioTest {

    private lateinit var jeltz: Person
    private lateinit var bridge: Bridge
    private lateinit var armchair: Armchair
    private lateinit var planet: Planet
    private val bridgeLocation = Location(10.0, 50.0)
    private val armchairLocation = Location(36.0, 121.0)

    @BeforeEach
    fun setUp() {
        jeltz = Person(CharacterNames.JELTZ.name, 70.0)
        jeltz.currentState = PersonState.STANDING
        jeltz.currentLocation = Location(0.0, 0.0)
        bridge = Bridge(BuildingState.BUILT, "Bridge", bridgeLocation, Dimensions(10.0, 2.0, 3.0))
        armchair = Armchair(ArmchairState.FREE, armchairLocation, "Armchair", Dimensions(1.0, 1.0, 1.0))
        planet = Planet(PlanetState.INHABITED, Dimensions(1000.0, 1000.0, 1000.0))
    }

    @Test
    fun shouldExecuteFullScenario() {
        var moveCommand = WalkCommand(jeltz, bridgeLocation)
        moveCommand.execute()

        assertEquals(bridgeLocation, jeltz.currentLocation, "${jeltz.displayName} should be at bridge location")
        assertEquals(PersonState.STANDING, jeltz.currentState, "${jeltz.displayName} should be standing after walking")

        bridge.crossBridge(jeltz)
        val otherSideLocation = Location(bridgeLocation.x + bridge.dimensions.length, bridgeLocation.z + bridge.dimensions.width)
        assertEquals(otherSideLocation, jeltz.currentLocation, "${jeltz.displayName} should be on the other side of the bridge")
        assertEquals(PersonState.STANDING, jeltz.currentState, "${jeltz.displayName} should be standing after crossing bridge")

        val destroyPlanetCmd = DestroyPlanetCommand(jeltz, planet)
        destroyPlanetCmd.execute()

        assertEquals(PlanetState.DESTROYED, planet.currentState, "Planet should be destroyed")
        assertEquals(MoodState.GRUMPY, jeltz.mood, "${jeltz.displayName} should be grumpy after destroying planet")

        moveCommand = WalkCommand(jeltz, armchairLocation)
        moveCommand.execute()

        assertEquals(armchairLocation, jeltz.currentLocation, "${jeltz.displayName} should be at armchair location")
        assertEquals(PersonState.STANDING, jeltz.currentState, "${jeltz.displayName} should be standing before sitting")

        val sitCmd = SitOnArmchairCommand(jeltz, armchair)
        sitCmd.execute()

        assertEquals(PersonState.SITTING, jeltz.currentState, "${jeltz.displayName} should be sitting")
        assertEquals(ArmchairState.OCCUPIED, armchair.initialState, "Armchair should be occupied")
        assertEquals(jeltz.displayName, armchair.seatLockOwner, "Jeltz should be the seat owner")
        assertEquals(MoodState.GRUMPY, jeltz.mood, "${jeltz.displayName} should remain grumpy after sitting")
    }

    @Test
    fun shouldHandleBridgeCrossingBothWays() {
        var moveCommand = WalkCommand(jeltz, bridgeLocation)
        moveCommand.execute()

        bridge.crossBridge(jeltz)
        val otherSideLocation = Location(bridgeLocation.x + bridge.dimensions.length, bridgeLocation.z + bridge.dimensions.width)
        assertEquals(otherSideLocation, jeltz.currentLocation, "${jeltz.displayName} should be on the other side")

        bridge.crossBridge(jeltz)
        assertEquals(bridgeLocation, jeltz.currentLocation, "${jeltz.displayName} should be back at start side")
    }

    @Test
    fun shouldNotSitOnArmchairBeforeCrossingBridge() {
        val sitCmd = SitOnArmchairCommand(jeltz, armchair)

        val exception = assertThrows<IllegalStateException>() {
            sitCmd.execute()
        }
        assertEquals(exception.message, "${jeltz.displayName} is not near ${armchair.displayName}")
    }

    @Test
    fun shouldDestroyPlanetAndThenSit() {
        val destroyPlanetCmd = DestroyPlanetCommand(jeltz, planet)
        destroyPlanetCmd.execute()

        assertEquals(PlanetState.DESTROYED, planet.currentState, "Planet should be destroyed")
        assertEquals(MoodState.GRUMPY, jeltz.mood, "${jeltz.displayName} should be grumpy")

        val moveCommand = WalkCommand(jeltz, armchairLocation)
        moveCommand.execute()

        val sitCmd = SitOnArmchairCommand(jeltz, armchair)
        sitCmd.execute()

        assertEquals(PersonState.SITTING, jeltz.currentState, "${jeltz.displayName} should be sitting after destroying planet")
        assertEquals(MoodState.GRUMPY, jeltz.mood, "${jeltz.displayName} should remain grumpy after sitting")
    }

    @Test
    fun shouldWalkToBridgeThenToArmchair() {
        var moveCommand = WalkCommand(jeltz, bridgeLocation)
        moveCommand.execute()
        assertEquals(bridgeLocation, jeltz.currentLocation, "${jeltz.displayName} should be at bridge")

        moveCommand = WalkCommand(jeltz, armchairLocation)
        moveCommand.execute()
        assertEquals(armchairLocation, jeltz.currentLocation, "${jeltz.displayName} should be at armchair")
    }
}