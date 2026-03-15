package domain.secant

import org.example.domain.Cosine
import org.example.domain.Secant
import org.example.domain.Sine
import org.example.utils.DEFAULT_ACCURACY
import org.example.utils.MathConstants
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
@Tag("integration")
@DisplayName("Secant integration tests")
class CosineIntegrationTest {

    private val SAMPLE_ARGUMENT = MathConstants.MY_PI.value
    private val SAMPLE_ACCURACY = DEFAULT_ACCURACY

    private lateinit var sineSpy: Sine
    private lateinit var cosineSpy: Cosine

    @BeforeEach
    fun setUp() {
        sineSpy = spy(Sine())
        cosineSpy = spy(Cosine(sineSpy))
    }

    @Test
    fun shouldCallSineFunction() {
        val secant = Secant(cosineSpy)

        val result: BigDecimal = secant(SAMPLE_ARGUMENT, SAMPLE_ACCURACY)
        verify(cosineSpy, atLeastOnce()).invoke(SAMPLE_ARGUMENT, SAMPLE_ACCURACY)
        
        val expectedSineArgument = MathConstants.MY_PI.value.divide(BigDecimal(2)) - SAMPLE_ARGUMENT
        verify(sineSpy, atLeastOnce()).invoke(expectedSineArgument, SAMPLE_ACCURACY)

        assertEquals(-1.0, result.toDouble(), DEFAULT_ACCURACY.toDouble())
    }
}