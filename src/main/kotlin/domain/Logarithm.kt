package org.example.domain

import org.example.utils.ACCURACY_MARGIN
import java.math.BigDecimal
import java.math.RoundingMode

class Logarithm(
    private val base: BigDecimal,
    private val natLog: NatLog,
    functionName: String = "log$base(x)"
) : MathFunction(functionName) {

    override fun invoke(x: BigDecimal, e: BigDecimal): BigDecimal {
        val numerator = natLog(x, e)
        val denominator = natLog(base, e)

        return numerator.divide(
            denominator,
            e.scale() + ACCURACY_MARGIN,
            RoundingMode.HALF_EVEN
        )
    }
}