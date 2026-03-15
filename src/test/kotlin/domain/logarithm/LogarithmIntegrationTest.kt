package domain.logarithm

import org.example.domain.Logarithm
import org.example.domain.NatLog
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
@Tag("integration")
@DisplayName("Logarithm integration tests")
class LogarithmIntegrationTest {

    private val SAMPLE_ARGUMENT = BigDecimal("8")
    private val BASE = BigDecimal("2")
    private val SAMPLE_ACCURACY = DEFAULT_ACCURACY

    @Spy
    lateinit var natLogSpy: NatLog

    @Test
    fun shouldCallNatLogFunction() {
        val log = Logarithm(BASE, natLogSpy)
        val result = log(SAMPLE_ARGUMENT, SAMPLE_ACCURACY)

        verify(natLogSpy, atLeastOnce()).invoke(SAMPLE_ARGUMENT, SAMPLE_ACCURACY)

        assertEquals(3.0, result.toDouble(), DEFAULT_ACCURACY.toDouble())

    }
}