package com.sashka.commands

import com.sashka.enums.PersonState
import com.sashka.models.Location
import com.sashka.models.Person

class WalkCommand(private val person: Person, private val destination: Location): Executable {

    override fun execute() {
        person.transitionTo(PersonState.WALKING)
        println("${person.displayName} is walking from ${person.currentLocation} to $destination")
        person.currentLocation = destination
        person.transitionTo(PersonState.STANDING)
        println("${person.displayName} is now in ${person.currentLocation}\n")
    }
}