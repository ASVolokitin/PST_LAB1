package domain

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.numericDouble
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.example.domain.NatLog
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.math.ln

class NatLogTest {

    @Test
    fun shouldThrowExceptionTest() {
        val e = BigDecimal(DEFAULT_ACCURACY.toString())

        assertThrows<ArithmeticException>("Failed on case: Zero") {
            NatLog()(BigDecimal.ZERO, e)
        }

        assertThrows<ArithmeticException>("Failed on case: Negative") {
            NatLog()(BigDecimal("-1.0"), e)
        }
    }

    @Test
    fun propertyTest() = runTest {
        val e = DEFAULT_ACCURACY.divide(BigDecimal(100))

        checkAll(Arb.numericDouble(min = 1e-32, max = 1e32)) { x ->
            val expected = ln(x)
            val result = NatLog()(BigDecimal(x), e)
            result.toDouble() shouldBe (expected plusOrMinus DEFAULT_ACCURACY.toDouble())
        }
    }
}