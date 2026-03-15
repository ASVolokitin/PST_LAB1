package domain.logarithm

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.numericDouble
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.example.domain.Logarithm
import org.example.domain.NatLog
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.Spy
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.log
import kotlin.math.max
import kotlin.test.assertEquals

@Tag("integration")
@DisplayName("Logarithm integration tests")
class LogarithmIntegrationTest {

    private lateinit var logarithm: Logarithm

    private val SAMPLE_ARGUMENT = BigDecimal.TEN
    private val SAMPLE_ACCURACY = DEFAULT_ACCURACY
    private val BASE = BigDecimal(2)


    @Spy
    lateinit var natLogSpy: NatLog

    @BeforeEach
    fun init() {
        natLogSpy = spy(NatLog())
        logarithm = Logarithm(BASE, natLogSpy)
    }

    @Test
    fun shouldCallNatLogFunction() {
        val result: BigDecimal = logarithm(SAMPLE_ARGUMENT, SAMPLE_ACCURACY)
        verify(natLogSpy, atLeastOnce()).invoke(
            SAMPLE_ARGUMENT,
            SAMPLE_ACCURACY
        )
        verify(natLogSpy, atLeastOnce()).invoke(
            BASE,
            SAMPLE_ACCURACY
        )
        assertEquals(3.3219280948873675, result.toDouble(), DEFAULT_ACCURACY.toDouble())
    }

    @Test
    fun shouldCalculateEdgeCases() {
        val cases = listOf(
            Triple("One", 1.0, 0.0),
            Triple("Base", BASE.toDouble(), 1.0),
        )

        for ((name, x, expected) in cases) {
            val result = logarithm(BigDecimal(x)).toDouble()
            Assertions.assertEquals(expected, result, DEFAULT_ACCURACY.toDouble(), "Failed on case: $name")
        }
    }

    @Test
    fun propertyTest() = runTest {
        checkAll(Arb.numericDouble(min = 1e-32, max = 1e32)) { x ->
            val expected = log(x, BASE.toDouble())
            val result = logarithm(BigDecimal(x))
            result.toDouble() shouldBe (expected plusOrMinus max(DEFAULT_ACCURACY.toDouble() * abs(expected), DEFAULT_ACCURACY.toDouble()))
        }
    }
}