package domain.eqsystem

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.numericDouble
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.example.domain.Cosine
import org.example.domain.EquationSystem
import org.example.domain.Logarithm
import org.example.domain.NatLog
import org.example.domain.Secant
import org.example.domain.Sine
import org.example.utils.ACCURACY_MARGIN
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.log
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin
@Tag("unit")
@DisplayName("Equation System module tests")
class EquationSystemModuleTest {

    private lateinit var system: EquationSystem

    @BeforeEach
    fun init() {
        val sine = Sine()
        val cosine = Cosine(sine)
        val secant = Secant(cosine)

        val natLog = NatLog()
        val log2 = Logarithm(BigDecimal(2), natLog)
        val log3 = Logarithm(BigDecimal(3), natLog)
        val log5 = Logarithm(BigDecimal(5), natLog)
        val log10 = Logarithm(BigDecimal(10), natLog)

        system = EquationSystem(sine, secant, log2, log3, log5, log10)
    }

    @Test
    fun exceptionsTest() {
        val e = BigDecimal(DEFAULT_ACCURACY.toString())

        assertThrows<ArithmeticException>("Failed on case: x = 1 (Division by zero)") {
            system(BigDecimal.ONE, e)
        }
    }

    @Test
    fun propertyTestXLessOrEqualZero() = runTest {
        val e = DEFAULT_ACCURACY

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