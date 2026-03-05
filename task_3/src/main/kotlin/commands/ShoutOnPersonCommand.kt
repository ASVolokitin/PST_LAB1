package com.sashka.commands

import com.sashka.models.Person
import com.sashka.states.MoodState

class ShoutOnPersonCommand(val initiator: Person, val victim: Person): Executable {
    override fun invoke() {
        if (initiator.mood != MoodState.GRUMPY) throw IllegalStateException("${initiator.displayName} is currently ${initiator.mood} and is not able to shout")
        println("${initiator.displayName} shouted on ${victim.displayName}")
        victim.changeMood(MoodState.GRUMPY)
        initiator.changeMood(MoodState.CALM)
    }
}