package com.sashka.commands

import com.sashka.models.Person
import com.sashka.models.Armchair
import com.sashka.states.ArmchairState
import com.sashka.enums.PersonState
import com.sashka.models.Dimensions
import com.sashka.states.MoodState
import com.sashka.models.Location
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows

class SitOnArmchairCommandTest {

    private lateinit var person: Person
    private lateinit var armchair: Armchair
    private lateinit var command: SitOnArmchairCommand

    @BeforeEach
    fun setUp() {
        person = Person("Balbes")
        person.currentState = PersonState.STANDING
        armchair = Armchair(ArmchairState.FREE, Location(0.0, 0.0))
        command = SitOnArmchairCommand(person, armchair)
    }

    @Test
    fun shouldTakeFreeArmchair() {
        command.execute()

        assertEquals(PersonState.SITTING, person.currentState, "${person.displayName} should be sitting")
        assertEquals(ArmchairState.OCCUPIED, armchair.initialState, "${armchair.displayName} should be marked as occupied")
        assertEquals(person.displayName, armchair.seatLockOwner, "Seat lock owner of ${armchair.displayName} should be set")
    }

    @Test
    fun shouldBreakArmchairAndBecomeGrumpy() {

        val heavyPerson = Person("Heavy Balbes", weight = 20000.0)

        val lightArmchair = Armchair(ArmchairState.FREE, Location(0.0, 0.0),
            dimensions = Dimensions(1.0, 1.0, 1.0)
        )

        val heavyCommand = SitOnArmchairCommand(heavyPerson, lightArmchair)
        heavyCommand.execute()

        assertEquals(PersonState.STANDING, heavyPerson.currentState, "${person.displayName} should be ${PersonState.STANDING}")
        assertEquals(MoodState.GRUMPY, heavyPerson.mood, "${person.displayName} should be ${MoodState.GRUMPY}")
        assertEquals(ArmchairState.BROKEN, lightArmchair.initialState, "${armchair.displayName} should be ${ArmchairState.BROKEN}")
        assertEquals(heavyPerson.displayName, lightArmchair.seatLockOwner, "The culprit must be identified")
    }

    @Test
    fun shouldThrowExceptionWhenPersonIsAwayFromArmchair() {
        
        val balbesFaraway = Person(PersonState.STANDING, initialLocation = Location(10.0, 10.0))
        val cmd = SitOnArmchairCommand(balbesFaraway, armchair)

        val exception = assertThrows<IllegalStateException> {
            cmd.execute()
        }
        
        assertEquals(exception.message, "${balbesFaraway.displayName} is not near ${armchair.displayName}", "Should throw an exception")
    }

    @Test
    fun shouldThrowAnExceptionWhenArmchairIsAlreadyOccupied() {
        val firstPerson = Person("FirstBalbes")
        val secondPerson = Person("SecondBalbes")
        val occupiedArmchair = Armchair(ArmchairState.FREE, Location(0.0, 0.0))

        SitOnArmchairCommand(firstPerson, occupiedArmchair).execute()
        val secondCmd = SitOnArmchairCommand(secondPerson, occupiedArmchair)

        val exception = assertThrows<IllegalArgumentException> {
            secondCmd.execute()
        }

        assertEquals(exception.message, "${armchair.displayName} is already occupied by ${firstPerson.displayName}")
        assertEquals(occupiedArmchair.seatLockOwner, firstPerson.displayName, "Seat lock owner should be the same")
    }

    @Test
    fun shouldThrowExceptionWhenArmchairBroken() {
        val brokenArmchair = Armchair(ArmchairState.BROKEN, Location(0.0, 0.0))
        val cmd = SitOnArmchairCommand(person, brokenArmchair)

        val exception = assertThrows<IllegalArgumentException> {
            cmd.execute()
        }

        assertEquals(exception.message, "${brokenArmchair.displayName} is already broken by ${brokenArmchair.seatLockOwner}", "Should throw an exception")
    }

}