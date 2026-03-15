package domain.equation_system

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.numericDouble
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.example.domain.Cosine
import org.example.domain.EquationSystem
import org.example.domain.Logarithm
import org.example.domain.NatLog
import org.example.domain.Secant
import org.example.domain.Sine
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.Spy
import java.math.BigDecimal
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.log
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin

@Tag("integration")
@DisplayName("Equation System integration tests")
class EquationSystemIntegrationTest {

    private lateinit var equationSystem: EquationSystem

    private val SAMPLE_ACCURACY = DEFAULT_ACCURACY

    @Spy
    lateinit var sineSpy: Sine

    @Spy
    lateinit var secantSpy: Secant

    @Spy
    lateinit var log2Spy: Logarithm

    @Spy
    lateinit var log3Spy: Logarithm

    @Spy
    lateinit var log5Spy: Logarithm

    @Spy
    lateinit var log10Spy: Logarithm

    @BeforeEach
    fun init() {
        val natLog = NatLog()
        sineSpy = spy(Sine())
        secantSpy = spy(Secant(Cosine(sineSpy)))
        log2Spy = spy(Logarithm(BigDecimal(2), natLog))
        log3Spy = spy(Logarithm(BigDecimal(3), natLog))
        log5Spy = spy(Logarithm(BigDecimal(5), natLog))
        log10Spy = spy(Logarithm(BigDecimal(10), natLog))

        equationSystem = EquationSystem(sineSpy, secantSpy, log2Spy, log3Spy, log5Spy, log10Spy)
    }

    @Test
    fun shouldCallCorrectFunctionsForNegativeX() {
        val arg = BigDecimal("-1.0")
        equationSystem(arg, SAMPLE_ACCURACY)

        verify(sineSpy).invoke(arg, SAMPLE_ACCURACY)
        verify(secantSpy).invoke(arg, SAMPLE_ACCURACY)
    }

    @Test
    fun shouldCallCorrectFunctionsForPositiveX() {
        val arg = BigDecimal("2.0")
        equationSystem(arg, SAMPLE_ACCURACY)

        verify(log2Spy).invoke(arg, DEFAULT_ACCURACY)
        verify(log3Spy).invoke(arg, DEFAULT_ACCURACY)
        verify(log5Spy).invoke(arg, DEFAULT_ACCURACY)
        verify(log10Spy).invoke(arg, DEFAULT_ACCURACY)

    }
    
    @Test
    fun shouldThrowOnOne() {
        assertThrows<ArithmeticException> {
            equationSystem(BigDecimal.ONE, SAMPLE_ACCURACY)
        }
    }

    @Test
    fun shouldCalculateEdgeCases() {
        val cases = listOf(
            Triple("Zero", 0.0, 1.0),
            Triple("Negative Pi", -PI, -1.0)
        )

        for ((name, x, expected) in cases) {
            val result = equationSystem(BigDecimal(x)).toDouble()
            Assertions.assertEquals(expected, result, 1e-9, "Failed on case: $name")
        }
        
        val undefinedCases = listOf(
            "Minus Half Pi" to -PI / 2
        )
        for ((name, x) in undefinedCases) {
            val bigDecimalX = BigDecimal(x)
            doThrow(ArithmeticException::class.java).`when`(secantSpy).invoke(bigDecimalX, DEFAULT_ACCURACY)
            assertThrows<ArithmeticException>("Failed on case: $name") {
                 equationSystem(bigDecimalX)
            }
        }
    }

    @Test
    fun propertyTest() = runTest {
        checkAll(Arb.numericDouble(min = -1e32, max = 1e32)) { x ->
            if (x <= 0.0) {
                 if (abs(cos(x)) > 1e-5) {
                     val expected = (1 / cos(x) + sin(x)).pow(3)
                     val result = equationSystem(BigDecimal(x))
                     result.toDouble() shouldBe (expected plusOrMinus max(1e-5 * abs(expected), 1e-5))
                 }
            } else if (abs(x - 1.0) > 1e-5) {
                 val l2 = log(x, 2.0)
                 val l3 = log(x, 3.0)
                 val l5 = log(x, 5.0)
                 val l10 = log(x, 10.0)
                 
                 val numerator = (l3 / l2).pow(2) - (l2 + l10)
                 val denominator = l10 + l5
                 
                 if (abs(denominator) > 1e-5) {
                     val expected = l2 * (numerator / denominator)
                     val result = equationSystem(BigDecimal(x))
                     result.toDouble() shouldBe (expected plusOrMinus max(1e-5 * abs(expected), 1e-5))
                 }
            }
        }
    }
}