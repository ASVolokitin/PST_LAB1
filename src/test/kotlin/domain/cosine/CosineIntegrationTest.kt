package domain.cosine

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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.Spy
import java.math.BigDecimal
import kotlin.math.PI
import kotlin.math.cos
import kotlin.test.assertEquals

@Tag("integration")
@DisplayName("Cosine integration tests")
class CosineIntegrationTest {

    private lateinit var cosine: Cosine

    private val SAMPLE_ARGUMENT = MathConstants.MY_PI.value
    private val SAMPLE_ACCURACY = DEFAULT_ACCURACY

    @Spy
    lateinit var sineSpy: Sine

    @BeforeEach
    fun init() {
        sineSpy = spy(Sine())
        cosine = Cosine(sineSpy)
    }

    @Test
    fun shouldCallSineFunction() {
        cosine(SAMPLE_ARGUMENT, SAMPLE_ACCURACY)

        val result: BigDecimal = cosine(SAMPLE_ARGUMENT, SAMPLE_ACCURACY)
        val expectedArgument = MathConstants.MY_PI.value.divide(BigDecimal(2)) - SAMPLE_ARGUMENT
        verify(sineSpy, atLeastOnce()).invoke(expectedArgument,SAMPLE_ACCURACY)
        assertEquals(-1.0, result.toDouble(), DEFAULT_ACCURACY.toDouble())
    }

    @Test
    fun shouldCalculateEdgeCases() {
        val cases = listOf(
            Triple("Zero", 0.0, 1.0),
            Triple("Negative Zero", -0.0, 1.0),
            Triple("Pi", PI, -1.0),
            Triple("Negative Pi", -PI, -1.0),
            Triple("Half Pi", PI / 2, 0.0),
            Triple("Negative Half Pi", -PI / 2, 0.0),
            Triple("Two Pi", 2 * PI, 1.0)
        )

        for ((name, x, expected) in cases) {
            val result = cosine(BigDecimal(x)).toDouble()
            assertEquals(expected, result, DEFAULT_ACCURACY.toDouble(), "Failed on case: $name")
        }
    }

    @Test
    fun propertyTest() = runTest {
        checkAll(Arb.numericDouble(min = -1e32, max = 1e32)) { x ->
            val expected = cos(x)
            val result = cosine(BigDecimal(x))
            result.toDouble() shouldBe (expected plusOrMinus DEFAULT_ACCURACY.toDouble())
        }
    }
}