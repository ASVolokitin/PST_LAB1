package domain.sine

import org.example.domain.Sine
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import java.math.BigDecimal
import kotlin.test.assertEquals

@Tag("unit")
@DisplayName("Sine unit tests")
class SineUnitTest {

    @ParameterizedTest(name = "x = {0}, expected sin(x) = {1}")
    @CsvFileSource(resources = ["/sin_x_data.csv"], numLinesToSkip = 1)
    fun shouldCalculateSineUsingCsvData(
        xStr: String,
        expectedSineStr: String
    ) {
        val sine = Sine()
        val x = BigDecimal(xStr)
        val expectedSineValue = BigDecimal(expectedSineStr)
        val accuracy = DEFAULT_ACCURACY

        val actualResult = sine(x, accuracy)

        assertEquals(expectedSineValue.toDouble(), actualResult.toDouble(), DEFAULT_ACCURACY.toDouble())
    }
}