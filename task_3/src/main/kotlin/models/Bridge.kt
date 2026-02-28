package com.sashka.models

import com.sashka.commands.WalkCommand
import com.sashka.states.BuildingState

class Bridge(
    initialState: BuildingState,
    name: String = "Bridge",
    initialLocation: Location = Location(0.0, 0.0),
    dimensions: Dimensions = Dimensions(10.0, 2.0, 3.0),
    weight: Double = dimensions.calculateValue() * 7700 / 10
) : Building(initialState, name, initialLocation, dimensions, weight = weight) {

    fun crossBridge(person: Person) {

        val tmp = Location(this.currentLocation.x + this.dimensions.length, this.currentLocation.z + this.dimensions.width)
        var destination: Location = person.currentLocation

        when (person.currentLocation) {
            this.currentLocation -> destination = tmp
            tmp -> destination = this.currentLocation
            else -> throw IllegalStateException("${person.displayName} is not near ${this.displayName}")
        }
        val crossBridgeCmd: WalkCommand = WalkCommand(person, destination)
        crossBridgeCmd()
        println("${person.displayName} crossed a ${this.displayName}")
    }
}