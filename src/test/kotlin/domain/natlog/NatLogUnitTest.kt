package domain.natlog

import org.example.domain.NatLog
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import java.math.BigDecimal
import kotlin.test.assertEquals

@Tag("unit")
@DisplayName("NatLog unit tests")
class NatLogUnitTest {

    @ParameterizedTest(name = "x = {0}, expected ln(x) = {1}")
    @CsvFileSource(resources = ["/natlog_data.csv"], numLinesToSkip = 1)
    fun shouldCalculateNatLogUsingCsvData(
        xStr: String,
        expectedLnStr: String
    ) {
        val natLog = NatLog()
        val x = BigDecimal(xStr)
        val expectedLnValue = BigDecimal(expectedLnStr)
        val accuracy = DEFAULT_ACCURACY

        val actualResult = natLog(x, accuracy)

        assertEquals(expectedLnValue.toDouble(), actualResult.toDouble(), DEFAULT_ACCURACY.toDouble())
    }
}