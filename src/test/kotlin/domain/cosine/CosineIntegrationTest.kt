package domain.cosine

import org.example.domain.Cosine
import org.example.domain.Sine
import org.example.utils.DEFAULT_ACCURACY
import org.example.utils.MathConstants
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
@Tag("integration")
@DisplayName("Cosine integration tests")
class CosineIntegrationTest {

    private val SAMPLE_ARGUMENT = MathConstants.MY_PI.value
    private val SAMPLE_ACCURACY = DEFAULT_ACCURACY

    @Spy
    lateinit var sineSpy: Sine

    @Test
    fun shouldCallSineFunction() {
        val cosine = Cosine(sineSpy)
        cosine(SAMPLE_ARGUMENT, SAMPLE_ACCURACY)

        val result: BigDecimal = cosine(SAMPLE_ARGUMENT, SAMPLE_ACCURACY)
        verify(sineSpy, atLeastOnce()).invoke(
            MathConstants.MY_PI.value.divide(BigDecimal(2)) - SAMPLE_ARGUMENT,
            SAMPLE_ACCURACY
        )
        assertEquals(-1.0, result.toDouble(), DEFAULT_ACCURACY.toDouble())
    }
}