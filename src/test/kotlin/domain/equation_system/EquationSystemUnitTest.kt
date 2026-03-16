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
import kotlin.math.abs
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

    @ParameterizedTest(name = "x = {0}, expected = {1}")
    @CsvFileSource(resources = ["/equation_system_x_data.csv"], numLinesToSkip = 1)
    fun shouldCalculateSystemUsingCsvData(
        xStr: String,
        expectedResultStr: String
    ) {
        val equationSystem = EquationSystem(sineMock, secantMock, log2Mock, log3Mock, log5Mock, log10Mock)
        val x = BigDecimal(xStr)
        val expectedResult = BigDecimal(expectedResultStr)
        val accuracy = DEFAULT_ACCURACY

        if (x <= BigDecimal.ZERO) {
            val baseVal = Math.cbrt(expectedResult.toDouble())
            willReturn(BigDecimal.ZERO).given(sineMock).invoke(x, accuracy)
            willReturn(BigDecimal(baseVal)).given(secantMock).invoke(x, accuracy)
        } else {
            val l2 = Math.log(x.toDouble()) / Math.log(2.0)
            val l3 = Math.log(x.toDouble()) / Math.log(3.0)
            val l5 = Math.log(x.toDouble()) / Math.log(5.0)
            val l10 = Math.log(x.toDouble()) / Math.log(10.0)

            willReturn(BigDecimal(l2)).given(log2Mock).invoke(x, accuracy)
            willReturn(BigDecimal(l3)).given(log3Mock).invoke(x, accuracy)
            willReturn(BigDecimal(l5)).given(log5Mock).invoke(x, accuracy)
            willReturn(BigDecimal(l10)).given(log10Mock).invoke(x, accuracy)
        }

        if (abs(x.toDouble() - 1.0) > 1e-9) {
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
             assertEquals(expectedResult.toDouble(), actualResult.toDouble(), 1e-4)
        }
    }
}