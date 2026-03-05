package org.example.domain

import org.example.utils.ACCURACY_MARGIN
import java.math.BigDecimal
import java.math.RoundingMode

class Logarithm: MathFunction() {
    operator fun invoke(base: BigDecimal, arg: BigDecimal, e: BigDecimal): BigDecimal {
        val numerator = NatLog()(arg, e)
        val denominator = NatLog()(base, e)

        return numerator.divide(
            denominator,
            e.scale() + ACCURACY_MARGIN,
            RoundingMode.HALF_EVEN
        )
    }
}