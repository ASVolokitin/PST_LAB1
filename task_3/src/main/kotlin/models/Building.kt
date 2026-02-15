package com.sashka.models

import com.sashka.states.BuildingState

open class Building(
    initialState: BuildingState,
    name: String = "Building",
    initialLocation: Location = Location(0.0, 0.0),
    dimensions: Dimensions,
    weight: Double
): Entity<BuildingState>(initialState, name, initialLocation, dimensions, weight)