package com.sashka.models

data class Dimensions(val length: Double, val width: Double, val height: Double) {

    fun calculateValue() = width * height * length
}