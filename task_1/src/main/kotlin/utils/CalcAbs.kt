package com.sashka.utils

class CalcAbs {

    companion object {
        fun abs(x: Double): Double {
            return if (x >= 0) x else -x
        }
    }
}