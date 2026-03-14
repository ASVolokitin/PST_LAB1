package org.example.domain

import org.example.utils.MathConstants
import java.math.BigDecimal

class Cosine(functionName: String = "cos(x)"): MathFunction(functionName) {
    override fun invoke(x: BigDecimal, e: BigDecimal): BigDecimal {
        return Sine()(MathConstants.MY_PI.value.divide(BigDecimal(2)) - x, e)
    }
}