package com.sashka.models

import com.sashka.enums.PersonState
import com.sashka.states.ArmchairState
import com.sashka.states.BuildingState

class Armchair(
    var initialState: ArmchairState,
    initialLocation: Location = Location(0.0, 0.0),
    name: String = "Armchair",
    dimensions: Dimensions = Dimensions(1.0, 1.0, 1.0),
    weight: Double = dimensions.calculateValue() * 1100 * 2.0 / 3.0,
) : Entity<ArmchairState>(initialState, name, initialLocation, dimensions, weight) {

    var seatLockOwner: String? = null

    fun takeASeat(person: Person): ArmchairState {

        if (person.currentState != PersonState.STANDING) throw IllegalStateException("${person.displayName} should be standing before trying to take a seat")

        if (person.currentLocation != this.currentLocation) throw IllegalStateException("${person.displayName} is not near ${this.displayName}")

        when (initialState) {
            ArmchairState.FREE -> {

                if (person.weight > this.weight) {
                    markAsBroken(person)
                    return this.initialState
                }

                this.initialState = ArmchairState.OCCUPIED
                println("${person.displayName} occupied ${this.displayName}")
                seatLockOwner = person.displayName
                return this.initialState
            }

            ArmchairState.OCCUPIED -> throw IllegalArgumentException("${this.displayName} is already occupied by $seatLockOwner")
            ArmchairState.BROKEN -> throw IllegalArgumentException("${this.displayName} is already broken by $seatLockOwner")
        }
    }

    private fun markAsBroken(person: Person) {
        this.initialState = ArmchairState.BROKEN
        this.seatLockOwner = person.displayName
        println("${person.displayName} broke ${this.displayName}")
    }
}