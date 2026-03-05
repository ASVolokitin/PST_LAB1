package com.sashka.utils

import com.sashka.utils.CalcAbs.Companion.abs
import com.sashka.utils.CalcSqrt.Companion.sqrt
import com.sashka.utils.Constants.Companion.PI

class CalcArctan {

    companion object {
        fun arctan(x: Double, n: Int = 100): Double {
            if (x.isNaN()) return Double.NaN

            if (x > 1e15) return (PI / 2.0) - (1.0 / x)
            if (x < -1e15) return (-PI / 2.0) - (1.0 / x)

            if (abs(x) > 0.5) {
                return 2.0 * arctan(x / (1.0 + sqrt(1.0 + x * x)), n)
            }

            var sum = 0.0
            val x2 = x * x
            var currentTerm = x

            for (i in 0..n) {
                val nextStep = currentTerm / (2.0 * i + 1)

                if (nextStep == 0.0) break

                sum += nextStep
                currentTerm *= -x2
            }

            return sum
        }
    }
}