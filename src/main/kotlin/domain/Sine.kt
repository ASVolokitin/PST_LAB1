package org.example.domain

import org.example.utils.ACCURACY_MARGIN
import org.example.utils.MAX_ITER_AMOUNT
import org.example.utils.MathConstants
import java.math.BigDecimal
import java.math.RoundingMode

class Sine: MathFunction() {
    override fun invoke(x: BigDecimal, e: BigDecimal): BigDecimal {

        val reducedX = x.remainder(MathConstants.MY_PI.value.multiply(BigDecimal(2)))

        var term = reducedX
        var result = term
        var iterCounter = 1

        while (term.abs() > e) {
            term = term.multiply(-reducedX.multiply(reducedX))
                .divide(
                    BigDecimal(2 * iterCounter)
                        .multiply(BigDecimal(2 * iterCounter + 1)),
                    e.scale() + ACCURACY_MARGIN,
                    RoundingMode.HALF_EVEN
                )
            result += term
            iterCounter++

            if (iterCounter > MAX_ITER_AMOUNT) break
        }

        return result
    }
}