package org.example.domain

import java.math.BigDecimal
import java.math.MathContext

class EquationSystem(
    private val sin: Sine,
    private val sec: Secant,
    private val log2: Logarithm,
    private val log3: Logarithm,
    private val log5: Logarithm,
    private val log10: Logarithm,
    functionName: String = "equation_system(x)"

) : MathFunction(functionName) {

    override fun invoke(x: BigDecimal, e: BigDecimal): BigDecimal {
        val mc = MathContext.DECIMAL64

        if (x.compareTo(BigDecimal.ZERO) <= 0) {
            return (sec(x, e) + sin(x, e)).pow(3)
        } else {
            if (x.compareTo(BigDecimal.ONE) == 0) {
                throw ArithmeticException("SOD: x could not be equal to 1")
            }

            val lx10 = log10(x, e)
            val lx2 = log2(x, e)

            val numerator = (log3(x, e).divide(lx2, mc)).pow(2) - (lx2 + lx10)
            val denominator = lx10 + log5(x, e)
            return lx2.multiply(numerator.divide(denominator, mc))
        }
    }
}