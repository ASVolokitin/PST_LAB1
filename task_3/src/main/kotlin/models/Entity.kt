package com.sashka.models

abstract class Entity<E: Enum<E>>(
    var currentState: E,
    val displayName: String,
    var currentLocation: Location,
    val dimensions: Dimensions,
    val weight: Double) {

    fun transitionTo(newState: E, errorMessage: String = "$displayName is already $newState") {
        require(currentState != newState) {errorMessage}
        currentState = newState
        println("$displayName is now $newState")
    }
}