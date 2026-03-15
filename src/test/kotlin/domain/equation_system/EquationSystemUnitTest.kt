package domain.equation_system

import org.example.domain.EquationSystem
import org.example.domain.Logarithm
import org.example.domain.Secant
import org.example.domain.Sine
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.mockito.BDDMockito.willReturn
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
@Tag("unit")
@DisplayName("Equation System unit tests")
class EquationSystemUnitTest {

    @Mock
    lateinit var sineMock: Sine

    @Mock
    lateinit var secantMock: Secant

    @Mock
    lateinit var log2Mock: Logarithm

    @Mock
    lateinit var log3Mock: Logarithm

    @Mock
    lateinit var log5Mock: Logarithm

    @Mock
    lateinit var log10Mock: Logarithm

    @ParameterizedTest(name = "x = {0}, expected = {7}")
    @CsvFileSource(resources = ["/equation_system_data.csv"], numLinesToSkip = 1)
    fun shouldCalculateSystemUsingCsvData(
        xStr: String,
        mockSinStr: String,
        mockSecStr: String,
        mockLog2Str: String,
        mockLog3Str: String,
        mockLog5Str: String,
        mockLog10Str: String,
        expectedResultStr: String
    ) {
        val equationSystem = EquationSystem(sineMock, secantMock, log2Mock, log3Mock, log5Mock, log10Mock)
        val x = BigDecimal(xStr)
        val expectedResult = BigDecimal(expectedResultStr)
        val accuracy = DEFAULT_ACCURACY

        if (x <= BigDecimal.ZERO) {
            willReturn(BigDecimal(mockSinStr)).given(sineMock).invoke(x, accuracy)
            willReturn(BigDecimal(mockSecStr)).given(secantMock).invoke(x, accuracy)
        } else {
            willReturn(BigDecimal(mockLog2Str)).given(log2Mock).invoke(x, accuracy)
            willReturn(BigDecimal(mockLog3Str)).given(log3Mock).invoke(x, accuracy)
            willReturn(BigDecimal(mockLog5Str)).given(log5Mock).invoke(x, accuracy)
            willReturn(BigDecimal(mockLog10Str)).given(log10Mock).invoke(x, accuracy)
        }

        val actualResult = equationSystem(x, accuracy)

        if (x <= BigDecimal.ZERO) {
            verify(sineMock).invoke(x, accuracy)
            verify(secantMock).invoke(x, accuracy)
        } else {
            verify(log2Mock).invoke(x, accuracy)
            verify(log3Mock).invoke(x, accuracy)
            verify(log5Mock).invoke(x, accuracy)
            verify(log10Mock).invoke(x, accuracy)
        }

        assertEquals(expectedResult.toDouble(), actualResult.toDouble(), DEFAULT_ACCURACY.toDouble())
    }
}