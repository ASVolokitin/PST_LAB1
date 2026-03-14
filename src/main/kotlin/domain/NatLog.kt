package org.example.domain

import org.example.utils.ACCURACY_MARGIN
import org.example.utils.MAX_ITER_AMOUNT
import org.example.utils.MathConstants
import java.math.BigDecimal
import java.math.RoundingMode

class NatLog(functionName: String = "ln(x)") : MathFunction(functionName) {

    override fun invoke(x: BigDecimal, e: BigDecimal): BigDecimal {
        if (x.compareTo(BigDecimal.ZERO) <= 0) {
            throw ArithmeticException("Natural logarithm could not be calculated for x = $x (x >= 0 only)")
        }
        if (x.compareTo(BigDecimal.ONE) == 0) {
            return BigDecimal.ZERO
        }


        val n = x.precision() - x.scale() - 1
        val reducedX = x.movePointLeft(n)

        val z1 = reducedX.subtract(BigDecimal.ONE).divide(reducedX.add(BigDecimal.ONE), e.scale() + ACCURACY_MARGIN, RoundingMode.HALF_DOWN)
        val z2 = z1.pow(2)
        var result = BigDecimal.ZERO
        var term = z1
        var i = 1L

        do {
            result = result.add(term.divide(BigDecimal.valueOf(i), e.scale() + ACCURACY_MARGIN, RoundingMode.HALF_DOWN))
            term = term.multiply(z2)
            i += 2
        } while (term.abs().compareTo(e) > 0 && i < MAX_ITER_AMOUNT)

        val seriesResult = result.multiply(BigDecimal.valueOf(2).setScale(e.scale() + ACCURACY_MARGIN, RoundingMode.HALF_DOWN))

        if (n == 0) return seriesResult

        val nLn10 = BigDecimal.valueOf(n.toLong())
            .multiply(MathConstants.LN10.value)
            .setScale(e.scale() + ACCURACY_MARGIN, RoundingMode.HALF_DOWN)

        return seriesResult.add(nLn10)
    }
}