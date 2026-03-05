package com.sashka.models

import com.sashka.enums.PersonState
import com.sashka.states.MoodState

class Person(
    initialState: PersonState,
    name: String = "Person",
    mass: Double = 80.0,
    initialLocation: Location = Location(0.0, 0.0),
    dimensions: Dimensions = Dimensions(0.5, 0.5, 1.8),
    var mood: MoodState = MoodState.CALM
    ):
    Entity<PersonState>(initialState, name, initialLocation, dimensions, weight = mass) {
        constructor(name: String): this(PersonState.STANDING, name)
        constructor(name: String, weight: Double): this(PersonState.STANDING, name, weight)

    fun changeMood(newMood: MoodState, errorMessage: String = "$displayName is already $newMood") {
        require(mood != newMood) {errorMessage}
        mood = newMood
        println("$displayName is now feeling $mood")
    }
}