package com.sashka.commands

import com.sashka.enums.CharacterNames
import com.sashka.models.Person
import com.sashka.models.Planet
import com.sashka.states.MoodState
import com.sashka.states.PlanetState

class DestroyPlanetCommand(val initiator: Person, val victim: Planet) : Executable {
    override fun invoke() {
        victim.currentState = PlanetState.DESTROYED
        println("\n${initiator.displayName} destroyed ${victim.displayName}")
            if (initiator.displayName == CharacterNames.JELTZ.name && initiator.mood != MoodState.GRUMPY) {
                initiator.changeMood(MoodState.GRUMPY)
            }
    }
}