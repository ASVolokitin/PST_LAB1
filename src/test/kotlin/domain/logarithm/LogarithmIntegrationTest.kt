package domain.logarithm

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.numericDouble
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.example.domain.Logarithm
import org.example.domain.NatLog
import org.example.utils.DEFAULT_ACCURACY
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.log
import kotlin.math.max

class LogarithmIntegrationTest : FeatureSpec({

    val BASE = BigDecimal(2)
    val natLogSpy = spy(NatLog())
    val logarithm = Logarithm(BASE, natLogSpy)

    val SAMPLE_ARGUMENT = BigDecimal.TEN
    val SAMPLE_ACCURACY = DEFAULT_ACCURACY

    feature("Logarithm function calculation") {
        scenario("it should call the natural log function for both argument and base") {
            val result: BigDecimal = logarithm(SAMPLE_ARGUMENT, SAMPLE_ACCURACY)
            verify(natLogSpy, atLeastOnce()).invoke(
                SAMPLE_ARGUMENT,
                SAMPLE_ACCURACY
            )
            verify(natLogSpy, atLeastOnce()).invoke(
                BASE,
                SAMPLE_ACCURACY
            )
            result.toDouble() shouldBe (3.3219280948873675 plusOrMinus DEFAULT_ACCURACY.toDouble())
        }

        scenario("it should calculate edge cases correctly") {
            val cases = listOf(
                "One" to (1.0 to 0.0),
                "Base" to (BASE.toDouble() to 1.0),
            )

            for ((name, values) in cases) {
                val (x, expected) = values
                val result = logarithm(BigDecimal(x)).toDouble()
                result shouldBe (expected plusOrMinus DEFAULT_ACCURACY.toDouble())
            }
        }

        scenario("it should align with kotlin's log function for various inputs") {
            runTest {
                checkAll(Arb.numericDouble(min = 1e-32, max = 1e32)) { x ->
                    val expected = log(x, BASE.toDouble())
                    val result = logarithm(BigDecimal(x))
                    result.toDouble() shouldBe (expected plusOrMinus max(DEFAULT_ACCURACY.toDouble() * abs(expected), DEFAULT_ACCURACY.toDouble()))
                }
            }
        }
    }
})