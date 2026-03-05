package org.example.domain

import org.example.utils.MathConstants
import java.math.BigDecimal

class Cosine: MathFunction() {
    override fun invoke(x: BigDecimal, e: BigDecimal): BigDecimal {
        return Sine()(x + MathConstants.MY_PI.value.divide(BigDecimal(2)), e)
    }
}