package org.example.domain

import org.example.utils.MathConstants
import java.math.BigDecimal

class Cosine(
    private val sine: Sine,
    functionName: String = "cos(x)"
): MathFunction(functionName) {
    override fun invoke(x: BigDecimal, e: BigDecimal): BigDecimal {
        return sine(MathConstants.MY_PI.value.divide(BigDecimal(2)) - x, e)
    }
}