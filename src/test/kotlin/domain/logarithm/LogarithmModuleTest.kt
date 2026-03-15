package domain.logarithm

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.numericDouble
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.example.domain.Logarithm
import org.example.domain.NatLog
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.math.log

@Tag("unit")
@DisplayName("Logarithm of an arbitrary base module tests")
class LogarithmModuleTest {

    private lateinit var natLog: NatLog

    @BeforeEach
    fun init() {
        natLog = NatLog()
    }


    @Test
    fun exceptionsTest() {
        val e = BigDecimal(DEFAULT_ACCURACY.toString())

        assertThrows<ArithmeticException>("Failed on case: Arg Zero") {
            Logarithm(BigDecimal(10), natLog)(BigDecimal.ZERO, e)
        }

        assertThrows<ArithmeticException>("Failed on case: Arg Negative") {
            Logarithm(BigDecimal(10), natLog)(BigDecimal("-5.0"), e)
        }

        assertThrows<ArithmeticException>("Failed on case: Base Zero") {
            Logarithm(BigDecimal.ZERO, natLog)(BigDecimal("10.0"), e)
        }

        assertThrows<ArithmeticException>("Failed on case: Base One") {
            Logarithm(BigDecimal.ONE, natLog)(BigDecimal("10.0"), e)
        }
    }

    @Test
    fun propertyTest() = runTest {
        val e = DEFAULT_ACCURACY

        val baseArb = Arb.numericDouble(min = 0 + 1e-32, max = 1e32).filter { it.compareTo(1) != 0 }
        val argArb = Arb.numericDouble(min = 0 + 1e-32, max = 1e32)

        checkAll(baseArb, argArb) { base, arg ->
            val expected = log(arg, base)
            val result = Logarithm(BigDecimal(base), natLog)(BigDecimal(arg), e)
            result.toDouble() shouldBe (expected plusOrMinus DEFAULT_ACCURACY.toDouble())
        }
    }
}