package domain.secant

import org.example.domain.Cosine
import org.example.domain.Secant
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.mockito.BDDMockito.willReturn
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
@Tag("unit")
@DisplayName("Secant unit tests")
class SecantUnitTest {
    @Mock
    lateinit var cosineMock: Cosine

    @ParameterizedTest(name = "x = {0}, expected sec(x) = {1}")
    @CsvFileSource(resources = ["/sec_x_data.csv"], numLinesToSkip = 1)
    fun shouldCalculateSecantUsingCsvData(
        xString: String,
        expectedSecantValueString: String
    ) {
        val secant = Secant(cosineMock)
        val x = BigDecimal(xString)
        val accuracy = DEFAULT_ACCURACY

        if (expectedSecantValueString == "Infinity") {
            willReturn(BigDecimal.ZERO).given(cosineMock).invoke(x, accuracy)
            assertThrows<ArithmeticException> {secant(x, accuracy)}
        } else {
            val expectedSecantValue = BigDecimal(expectedSecantValueString)
            val mockedCosineValue = BigDecimal.ONE.divide(expectedSecantValue, accuracy.scale(), java.math.RoundingMode.HALF_EVEN)
            willReturn(mockedCosineValue).given(cosineMock).invoke(x, accuracy)
            val actualResult = secant(x, accuracy)
            assertEquals(expectedSecantValue.toDouble(), actualResult.toDouble(), DEFAULT_ACCURACY.toDouble())
        }
    }
}