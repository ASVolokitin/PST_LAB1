package utils

import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import com.sashka.utils.CalcSqrt.Companion.sqrt as mySqrt
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.TestInstance
import kotlin.math.sqrt

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CalcSqrtTest {

    private val ACCURACY = 1e-14


    @Test
    fun propertyTest() = runTest {

        checkAll<Double>(Arb.double(min = 0.0)) { x ->
            val expected = sqrt(x)
            val result = mySqrt(x)
            val delta = (if (!expected.isNaN()) expected * ACCURACY else ACCURACY)

            assertEquals(expected, result, delta, "sqrt($x) should be $expected")
        }

    }

    @ParameterizedTest
    @ValueSource(doubles = [-1.0, -0.5, -10.0, -100.0])
    fun shouldReturnNaNForNegativeInput(x: Double) {
        val result = mySqrt(x)

        assertTrue(result.isNaN(), "sqrt($x) should be NaN for negative input")
    }

    @Test
    fun shouldReturnNaNForNaNInput() {
        val result = mySqrt(Double.NaN)

        assertTrue(result.isNaN(), "sqrt(NaN) should be NaN")
    }

    @Test
    fun shouldReturnPositiveInfinityForPositiveInfinity() {
        val result = mySqrt(Double.POSITIVE_INFINITY)

        assertEquals(Double.POSITIVE_INFINITY, result, "sqrt(+Inf) should be +Inf")
    }

    @ParameterizedTest
    @CsvSource(
        "1.0, 1.0",
        "4.0, 2.0",
        "9.0, 3.0",
        "16.0, 4.0",
        "25.0, 5.0",
        "36.0, 6.0",
        "49.0, 7.0",
        "64.0, 8.0",
        "81.0, 9.0",
        "100.0, 10.0",
        "121.0, 11.0",
        "144.0, 12.0"
    )
    fun shouldHandlePerfectSquares(x: Double, expected: Double) {
        val result = mySqrt(x)

        assertEquals(expected, result, ACCURACY, "sqrt($x) of perfect square should be exact")
    }

    @Test
    fun shouldBeIdempotent() = runTest {

        checkAll<Double>(Arb.double(min = 0.0)) { x ->
            val firstResult = mySqrt(x)
            val squared = firstResult * firstResult
            if (squared.isInfinite() && !x.isInfinite()) return@checkAll
            val secondResult = mySqrt(firstResult * firstResult)
            val delta = if (firstResult == 0.0 || firstResult.isNaN()) ACCURACY else firstResult * ACCURACY

            assertEquals(firstResult, secondResult, delta, "sqrt should be idempotent")
        }
    }
}