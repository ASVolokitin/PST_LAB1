package domain.cosine

import org.example.domain.Cosine
import org.example.domain.Sine
import org.example.utils.DEFAULT_ACCURACY
import org.example.utils.MathConstants
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.mockito.BDDMockito.willReturn
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
@Tag("integration")
@DisplayName("Cosine integration tests")
class CosineUnitTest {

    @Mock
    lateinit var sineMock: Sine

    @ParameterizedTest(name = "x = {0}, expected cos(x) = {1}")
    @CsvFileSource(resources = ["/cos_x_data.csv"], numLinesToSkip = 1)
    fun shouldCalculateCosineUsingCsvData(
        xStr: String,
        expectedCosineStr: String
    ) {
        val cosine = Cosine(sineMock)
        val x = BigDecimal(xStr)
        val expectedCosineValue = BigDecimal(expectedCosineStr)
        val accuracy = DEFAULT_ACCURACY

        val expectedSineArgument = MathConstants.MY_PI.value.divide(BigDecimal(2)) - x

        willReturn(expectedCosineValue).given(sineMock).invoke(expectedSineArgument, accuracy)
        val actualResult = cosine(x, accuracy)

        assertEquals(expectedCosineValue.toDouble(), actualResult.toDouble(), DEFAULT_ACCURACY.toDouble())
    }
}