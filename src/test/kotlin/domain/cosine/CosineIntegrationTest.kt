package domain.cosine

import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.numericDouble
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.example.domain.Cosine
import org.example.domain.Sine
import org.example.utils.DEFAULT_ACCURACY
import org.example.utils.MathConstants
import org.mockito.Mockito
import java.math.BigDecimal
import kotlin.math.PI
import kotlin.math.cos

class CosineIntegrationTest : ExpectSpec({

    val sineSpy = Mockito.spy(Sine())
    val cosine = Cosine(sineSpy)

    val SAMPLE_ARGUMENT = MathConstants.MY_PI.value
    val SAMPLE_ACCURACY = DEFAULT_ACCURACY

    context("Cosine function behavior") {
        expect("it calls the sine function") {
            val result: BigDecimal = cosine(SAMPLE_ARGUMENT, SAMPLE_ACCURACY)
            val expectedArgument = MathConstants.MY_PI.value.divide(BigDecimal(2)) - SAMPLE_ARGUMENT
            Mockito.verify(sineSpy, Mockito.atLeastOnce()).invoke(expectedArgument, SAMPLE_ACCURACY)
            result.toDouble() shouldBe (-1.0 plusOrMinus DEFAULT_ACCURACY.toDouble())
        }

        expect("it calculates edge cases correctly") {
            val cases = listOf(
                "Zero" to (0.0 to 1.0),
                "Negative Zero" to (-0.0 to 1.0),
                "Pi" to (PI to -1.0),
                "Negative Pi" to (-PI to -1.0),
                "Half Pi" to (PI / 2 to 0.0),
                "Negative Half Pi" to (-PI / 2 to 0.0),
                "Two Pi" to (2 * PI to 1.0)
            )

            for ((name, values) in cases) {
                val (x, expected) = values
                val result = cosine(BigDecimal(x)).toDouble()
                result shouldBe (expected plusOrMinus DEFAULT_ACCURACY.toDouble())
            }
        }

        expect("it matches kotlin's cos function for various inputs") {
            runTest {
                checkAll(Arb.numericDouble(min = -1e32, max = 1e32)) { x ->
                    val expected = cos(x)
                    val result = cosine(BigDecimal(x))
                    result.toDouble() shouldBe (expected plusOrMinus DEFAULT_ACCURACY.toDouble())
                }
            }
        }
    }
})