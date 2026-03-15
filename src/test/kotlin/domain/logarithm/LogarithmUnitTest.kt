package domain.logarithm

import org.example.domain.Logarithm
import org.example.domain.NatLog
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
@Tag("unit")
@DisplayName("Logarithm unit tests")
class LogarithmUnitTest {
    @Mock
    lateinit var natLogMock: NatLog

    private val BASE = BigDecimal(2)

    @ParameterizedTest(name = "log_2({0}) = {3}")
    @CsvFileSource(resources = ["/logarithm_data.csv"], numLinesToSkip = 1)
    fun shouldCalculateLogarithmUsingCsvData(
        xString: String,
        mockedLnXString: String,
        mockedLnBaseString: String,
        expectedLogValueString: String
    ) {
        val logarithm = Logarithm(BASE, natLogMock)
        val x = BigDecimal(xString)
        val mockedLnX = BigDecimal(mockedLnXString)
        val mockedLnBase = BigDecimal(mockedLnBaseString)
        val expectedLogValue = BigDecimal(expectedLogValueString)
        val accuracy = DEFAULT_ACCURACY

        doReturn(mockedLnX).`when`(natLogMock).invoke(x, accuracy)
        doReturn(mockedLnBase).`when`(natLogMock).invoke(BASE, accuracy)

        val actualResult = logarithm(x, accuracy)

        verify(natLogMock).invoke(x, accuracy)
        verify(natLogMock).invoke(BASE, accuracy)
        assertEquals(expectedLogValue.toDouble(), actualResult.toDouble(), DEFAULT_ACCURACY.toDouble())
    }
}