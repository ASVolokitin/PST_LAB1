package domain.natlog

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.numericDouble
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.example.domain.NatLog
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.math.ln

@Tag("unit")
@DisplayName("Natural logarithm module tests")
class NatLogModuleTest {

    private lateinit var natLog: NatLog

    @BeforeEach
    fun init() {
        natLog = NatLog()
    }


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
            val result = natLog(BigDecimal(x), e)
            result.toDouble() shouldBe (expected plusOrMinus DEFAULT_ACCURACY.toDouble())
        }
    }
}