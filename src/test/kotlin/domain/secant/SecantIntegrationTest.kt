package domain.secant

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.numericDouble
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.example.domain.Cosine
import org.example.domain.Secant
import org.example.domain.Sine
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.spy
import org.mockito.Spy
import java.math.BigDecimal
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.test.assertEquals

@Tag("integration")
@DisplayName("Secant integration tests")
class SecantIntegrationTest {

    private lateinit var secant: Secant

    private val SAMPLE_ARGUMENT = BigDecimal.ONE
    private val SAMPLE_ACCURACY = DEFAULT_ACCURACY

    @Spy
    lateinit var cosineSpy: Cosine

    @BeforeEach
    fun init() {
        val sineSpy = spy(Sine())
        cosineSpy = spy(Cosine(sineSpy))
        secant = Secant(cosineSpy)
    }

    @Test
    fun shouldCallCosineFunction() {
        val result: BigDecimal = secant(SAMPLE_ARGUMENT, SAMPLE_ACCURACY)
        Mockito.verify(cosineSpy, Mockito.atLeastOnce()).invoke(
            SAMPLE_ARGUMENT,
            SAMPLE_ACCURACY
        )
        assertEquals(1.8508157176809257, result.toDouble(), DEFAULT_ACCURACY.toDouble())
    }

    @Test
    fun shouldCalculateEdgeCases() {
        val cases = listOf(
            Triple("Zero", 0.0, 1.0),
            Triple("Pi", PI, -1.0),
            Triple("Negative Pi", -PI, -1.0),
            Triple("Two Pi", 2 * PI, 1.0)
        )

        for ((name, x, expected) in cases) {
            val result = secant(BigDecimal(x)).toDouble()
            Assertions.assertEquals(expected, result, DEFAULT_ACCURACY.toDouble(), "Failed on case: $name")
        }
    }

    @Test
    fun shouldThrowOnUndefined() {
        val undefinedCases = listOf(
            "Half Pi" to PI / 2,
            "Negative Half Pi" to -PI / 2
        )
        for ((name, x) in undefinedCases) {
            val bigDecimalX = BigDecimal(x)
            Mockito.doReturn(BigDecimal.ZERO).`when`(cosineSpy).invoke(bigDecimalX, DEFAULT_ACCURACY)
            assertThrows<ArithmeticException>("Failed on case: $name") {secant(bigDecimalX)}
        }
    }

    @Test
    fun propertyTest() = runTest {
        checkAll(Arb.numericDouble(min = -1e32, max = 1e32)) { x ->
                val expected = 1 / cos(x)
                val result = secant(BigDecimal(x))
                result.toDouble() shouldBe (expected plusOrMinus max(DEFAULT_ACCURACY.toDouble() * abs(expected), DEFAULT_ACCURACY.toDouble()))
        }
    }
}