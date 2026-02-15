package com.sashka

import com.sashka.commands.DestroyPlanetCommand
import com.sashka.commands.SitOnArmchairCommand
import com.sashka.commands.WalkCommand
import com.sashka.enums.CharacterNames
import com.sashka.models.Armchair
import com.sashka.models.Bridge
import com.sashka.models.Dimensions
import com.sashka.models.Location
import com.sashka.models.Person
import com.sashka.models.Planet
import com.sashka.states.ArmchairState
import com.sashka.states.BuildingState
import com.sashka.states.PlanetState

fun main() {

    val bridgeLocation: Location = Location(10.0, 50.0)
    val armchairLocation: Location = Location(36.0, 121.0)

    val jeltz = Person(CharacterNames.JELTZ.name, 70.0)
    val bridge = Bridge(BuildingState.BUILT, "Bridge", bridgeLocation)
    val armchair = Armchair(ArmchairState.FREE, armchairLocation)


    var moveCommand = WalkCommand(jeltz, bridgeLocation)
    moveCommand.execute()
    bridge.crossBridge(jeltz)

    val destroyPlanetCmd = DestroyPlanetCommand(jeltz, Planet(PlanetState.INHABITED, Dimensions(1000.0, 1000.0, 1000.0)))
    destroyPlanetCmd.execute()

    moveCommand = WalkCommand(jeltz, armchairLocation)
    moveCommand.execute()

    val sitCmd = SitOnArmchairCommand(jeltz, armchair)
    sitCmd.execute()
}