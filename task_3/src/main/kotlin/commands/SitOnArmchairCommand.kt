package com.sashka.commands

import com.sashka.enums.PersonState
import com.sashka.models.Armchair
import com.sashka.models.Person
import com.sashka.states.ArmchairState
import com.sashka.states.MoodState

class SitOnArmchairCommand(private val person: Person, private val armchair: Armchair): Executable {
    override fun execute() {
        val result = armchair.takeASeat(person)
        if (result == ArmchairState.OCCUPIED) person.transitionTo(PersonState.SITTING)
        else if (result == ArmchairState.BROKEN) {
            person.changeMood(MoodState.GRUMPY)
        }
    }
}