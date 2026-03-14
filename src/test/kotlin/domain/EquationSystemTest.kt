package domain

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.numericDouble
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.example.domain.EquationSystem
import org.example.utils.ACCURACY_MARGIN
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.math.*

class EquationSystemTest {

    @Test
    fun exceptionsTest() {
        val e = BigDecimal(DEFAULT_ACCURACY.toString())
        val system = EquationSystem()


        assertThrows<ArithmeticException>("Failed on case: x = 1 (Division by zero)") {
            system(BigDecimal.ONE, e)
        }
    }

    @Test
    fun propertyTestXLessOrEqualZero() = runTest {
        val e = DEFAULT_ACCURACY
        val system = EquationSystem()

        val negativeArb = Arb.numericDouble(min = -1e32, max = 0 - ACCURACY_MARGIN.toDouble())

        checkAll(negativeArb) { x ->
            val expected = (1.0 / cos(x) + sin(x)).pow(3)
            val result = system(BigDecimal(x), e)

            result.toDouble() shouldBe (expected plusOrMinus DEFAULT_ACCURACY.toDouble() * abs(expected))
        }
    }

    @Test
    fun propertyTestXGreaterThanZero() = runTest {
        val e = DEFAULT_ACCURACY
        val system = EquationSystem()

        val positiveArb = Arb.numericDouble(min = ACCURACY_MARGIN.toDouble(), max = 1e32)

        checkAll(positiveArb) { x ->
            val num = (log(x, 3.0) / log(x, 2.0)).pow(2) - (log(x, 2.0) + log10(x))
            val den = log10(x) + log(x, 5.0)
            val expected = log(x, 2.0) * (num / den)

            val result = system(BigDecimal(x), e)

            result.toDouble() shouldBe (expected plusOrMinus DEFAULT_ACCURACY.toDouble() * abs(expected))
        }
    }
}