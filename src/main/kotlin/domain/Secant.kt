package org.example.domain

import org.example.utils.ACCURACY_MARGIN
import java.math.BigDecimal
import java.math.RoundingMode

class Secant(functionName: String = "sec(x)") : MathFunction(functionName) {
    override fun invoke(x: BigDecimal, e: BigDecimal): BigDecimal {
        return BigDecimal(1)
            .divide(Cosine()(x, e),
            e.scale() + ACCURACY_MARGIN,
                RoundingMode.HALF_EVEN)
    }
}