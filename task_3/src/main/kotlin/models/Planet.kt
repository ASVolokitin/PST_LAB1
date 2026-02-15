package com.sashka.models

import com.sashka.states.PlanetState

class Planet(
    initialState: PlanetState,
    dimensions: Dimensions,
    name: String = "Planet",
    initialLocation: Location = Location(0.0, 0.0),
): Entity<PlanetState>(initialState,
    name,
    initialLocation,
    dimensions,
    dimensions.calculateValue() * Math.PI / 6 * 2500) {}