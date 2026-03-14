package org.example.domain

import org.example.utils.ACCURACY_MARGIN
import org.example.utils.MAX_ITER_AMOUNT
import org.example.utils.MathConstants
import java.math.BigDecimal
import java.math.RoundingMode

class Sine(functionName: String = "sin(x)") : MathFunction(functionName) {

    override fun invoke(x: BigDecimal, e: BigDecimal): BigDecimal {

        val pi = MathConstants.MY_PI.value
        val twoPi = pi * BigDecimal(2)
        val halfPi = pi.divide(BigDecimal(2))

        val k = x.divide(twoPi, 0, RoundingMode.DOWN)
        var r = x - (k * twoPi)

        if (r > pi) r -= twoPi

        if (r > halfPi) r = pi - r
        if (r < -halfPi) r = -pi - r

        val r2 = r * r

        var term = r
        var result = term
        var n = 1

        while (term.abs() > e) {

            val denom = BigDecimal((2 * n) * (2 * n + 1))

            term *= -r2.divide(denom, e.scale() + ACCURACY_MARGIN, RoundingMode.HALF_EVEN)

            result = result.add(term)

            n++
            if (n > MAX_ITER_AMOUNT) break
        }

        return result
    }
}