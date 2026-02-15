package commands

import com.sashka.commands.ShoutOnPersonCommand
import com.sashka.models.Person
import com.sashka.states.MoodState
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class ShoutOnPersonCommandTest {

    private lateinit var initiator: Person
    private lateinit var victim: Person
    private lateinit var shoutOnPersonCmd: ShoutOnPersonCommand

    @BeforeEach
    fun setUp() {
        initiator = Person("Initiator")
        victim = Person("Victim")
        shoutOnPersonCmd = ShoutOnPersonCommand(initiator, victim)
    }

    @Test
    fun shouldChangeMoodAfterShouting() {
        initiator.changeMood(MoodState.GRUMPY)
        shoutOnPersonCmd.execute()
        assertEquals(initiator.mood, MoodState.CALM)
        assertEquals(victim.mood, MoodState.GRUMPY)
    }

    @Test
    fun shouldThrowExceptionAfterTryingToShoutWhenCalm() {
        val exception = assertThrows<IllegalStateException> {
            shoutOnPersonCmd.execute()
        }
        assertEquals(exception.message, "${initiator.displayName} is currently ${initiator.mood} and is not able to shout")
    }
}