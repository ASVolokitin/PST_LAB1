package org.example.domain

import org.example.utils.DEFAULT_ACCURACY
import java.math.BigDecimal

open class MathFunction(val functionName: String): ICalculatable {
    override fun invoke(x: BigDecimal, e: BigDecimal): BigDecimal {
        return x
    }
    override fun invoke(x: BigDecimal): BigDecimal {
        return invoke(x, DEFAULT_ACCURACY)
    }
}