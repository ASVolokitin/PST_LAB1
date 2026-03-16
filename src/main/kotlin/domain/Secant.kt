package org.example.domain

import org.example.utils.ACCURACY_MARGIN
import java.math.BigDecimal
import java.math.RoundingMode

class Secant(
    private val cosine: Cosine,
    functionName: String = "sec(x)"
) : MathFunction(functionName) {
    override fun invoke(x: BigDecimal, e: BigDecimal): BigDecimal {
        val cosValue = cosine(x, e)

        if (cosValue.compareTo(BigDecimal.ZERO) == 0) {
            throw ArithmeticException("Secant undefined: cos($x) = 0")
        }

        return BigDecimal.ONE.divide(
            cosValue,e.scale() + ACCURACY_MARGIN,
            RoundingMode.HALF_EVEN
        )
    }
}