package org.example.domain

import java.math.BigDecimal
import java.math.MathContext

class EquationSystem(functionName: String = "equation_system(x)") : MathFunction(functionName) {

    val sin = Sine()
    val sec = Secant()
    val log2 = Logarithm(BigDecimal(2))
    val log3 = Logarithm(BigDecimal(3))
    val log5 = Logarithm(BigDecimal(5))
    val log10 = Logarithm(BigDecimal(10))

    override fun invoke(x: BigDecimal, e: BigDecimal): BigDecimal {
        val mc = MathContext.DECIMAL128

        if (x.compareTo(BigDecimal.ZERO) <= 0) {
            return (sec(x, e) + sin(x, e)).pow(3)
        } else {
            if (x.compareTo(BigDecimal.ONE) == 0) {
                throw ArithmeticException("SOD: x could not be equal to 1")
            }

            val numerator = (log3(x, e).divide(log2(x, e), mc)).pow(2) - (log2(x, e) + log10(x, e))
            val denominator = log10(x, e) + log5(x, e)
            return log2(x, e).multiply(numerator.divide(denominator, mc))
        }
    }
}