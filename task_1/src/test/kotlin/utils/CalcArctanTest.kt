package utils

import com.sashka.utils.CalcArctan.Companion.arctan

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream
import kotlin.math.abs
import kotlin.math.atan
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CalcArctanTest {

    private val DELTA = 1e-14

    fun rangeFromMinusOneToOne(): Stream<Double?>? = Stream.iterate(-1.0) { it + 0.2 }.limit(11)

    @ParameterizedTest
    @MethodSource("rangeFromMinusOneToOne")
    fun shouldCalculate(x: Double) {
        val result = arctan(x)
        val expected = atan(x)

        assertEquals(expected, result, DELTA, "arctan($x) should be $expected")
    }

    @Test
    fun shouldReturnNaNForNaNInput() {
        val result = arctan(Double.NaN)

        assertTrue(result.isNaN(), "arctan(NaN) should be NaN")
    }

    @Test
    fun shouldReturnZeroForZeroInput() {
        val result = arctan(0.0)
        assertEquals(result, 0.0, "arctan(0) should be 0")
    }

    @ParameterizedTest
    @ValueSource(doubles = [Double.NEGATIVE_INFINITY, Double.MIN_VALUE, Double.MAX_VALUE, Double.POSITIVE_INFINITY])
    fun shouldCalculateForHugeValues(x: Double) {
        val result = arctan(x)
        val expected = atan(x)

        assertEquals(expected, result, DELTA, "arctan($x) should be $expected")
    }

    @Test
    fun shouldConvergeWithMoreIterations() {
        val x = 0.9
        val resultWithFewIter = arctan(x, 5)
        val resultWithManyIter = arctan(x, 50)
        val exact = atan(x)

        assertTrue(
            abs(resultWithManyIter - exact) < abs(resultWithFewIter - exact),
            "More iterations should give more accurate result"
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["-1e-20", "-1e-17", "-1e-14", "1e-14", "1e-17", "1e-20"])
    fun shouldHandleVerySmallValues(x: Double) {
        val actual = arctan(x)
        val expected = atan(x)

        assertEquals(expected, actual, DELTA, "arctan($x) should handle very small values")
    }

    @Test
    fun shouldBeOddFunction() {
        val x = Math.random() * 2 - 1
        val posResult = arctan(x)
        val negResult = arctan(-x)

        assertEquals(posResult, -negResult, DELTA, "arctan should be odd function")
    }

    @Test
    fun shouldCalculateRandomValue() {
        val numberOfIterations: Int = 1_000_000
        for (i in 1..numberOfIterations) {
            val argument: Double = Double.fromBits(Random.nextLong())
            val expectedValue: Double = atan(argument)
            val actualValue: Double = arctan(argument)
            assertEquals(expectedValue, actualValue, DELTA, "[TEST $i of $numberOfIterations] for x = $argument")
        }
        println("[OK] Passed $numberOfIterations random tests")
    }
}