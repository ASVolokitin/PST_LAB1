package models

import com.sashka.commands.SitOnArmchairCommand
import com.sashka.enums.PersonState
import com.sashka.models.Armchair
import com.sashka.models.Person
import com.sashka.states.ArmchairState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ArmchairTest {

    @Test
    fun shouldThrowExceptionWhenPersonIsNotStanding() {
        val armchair = Armchair(initialState = ArmchairState.FREE)
        val sittingPerson = Person("SittingBalbes")
        sittingPerson.transitionTo(PersonState.SITTING)
        val cmd = SitOnArmchairCommand(sittingPerson, armchair)

        val exception = assertThrows<IllegalStateException> {
            cmd.execute()
        }

        assertEquals(exception.message, "${sittingPerson.displayName} should be standing before trying to take a seat", "Should throw an exception when person is not standing")
    }
}