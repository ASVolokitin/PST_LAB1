package utils

import com.sashka.utils.CalcAbs.Companion.abs
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.random.Random
import kotlin.math.abs as kotlinAbs

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CalcAbsTest {

    private val DELTA = 1e-14

    fun randomDoubles() = generateSequence { Double.fromBits(Random.nextDouble().toLong()) }
        .take(100)
        .map { Arguments.of(it, kotlinAbs(it)) }
        .toList()
        .stream()

    @ParameterizedTest
    @MethodSource("randomDoubles")
    fun shouldCalculateForRandomValues(x: Double, expected: Double) {
        val result = abs(x)

        assertEquals(expected, result, DELTA, "abs($x) should be $expected")
    }

    @Test
    fun shouldReturnNaNForNaNInput() {
        val result = abs(Double.NaN)

        assertTrue(result.isNaN(), "abs(NaN) should be NaN")
    }

    @Test
    fun shouldReturnPositiveInfinityForNegativeInfinity() {
        val result = abs(Double.NEGATIVE_INFINITY)

        assertEquals(Double.POSITIVE_INFINITY, result, "abs(-Inf) should be +Inf")
    }

    @Test
    fun shouldReturnPositiveInfinityForPositiveInfinity() {
        val result = abs(Double.POSITIVE_INFINITY)

        assertEquals(Double.POSITIVE_INFINITY, result, "abs(+Inf) should be +Inf")
    }

    @ParameterizedTest
    @CsvSource(
        "-0.0, 0.0",
        "0.0, 0.0"
    )
    fun shouldHandleZero(x: Double, expected: Double) {
        val result = abs(x)

        assertEquals(expected, result, DELTA, "abs($x) should be $expected")
    }

    @Test
    fun shouldBeIdempotent() {
        val values = listOf(-5.0, 3.0, 0.0, -0.5, 10.0)

        for (x in values) {
            val firstResult = abs(x)
            val secondResult = abs(firstResult)

            assertEquals(firstResult, secondResult, DELTA, "abs(abs($x)) should equal abs($x)")
        }
    }
}