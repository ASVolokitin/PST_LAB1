package domain.sine

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.numericDouble
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.example.domain.Sine
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.collections.iterator
import kotlin.math.PI
import kotlin.math.sin

@Tag("unit")
@DisplayName("Sine module tests")
class SineModuleTest {

    private lateinit var sine: Sine

    @BeforeEach
    fun init() {
        sine = Sine()
    }

    @Test
    fun edgeCasesTest() {
        val cases = mapOf(
            "Zero" to 0.0,
            "Negative Zero" to -0.0,
            "Pi" to PI,
            "Negative Pi" to -PI,
            "Half Pi" to PI / 2,
            "Negative Half Pi" to -PI / 2,
            "Two Pi" to 2 * PI,
        )

        for ((name, x) in cases) {
            val expected = sin(x)
            val result = sine(BigDecimal(x)).toDouble()
            Assertions.assertEquals(expected, result, DEFAULT_ACCURACY.toDouble(), "Failed on case: $name")
        }
    }

    @Test
    fun propertyTest() = runTest {
        checkAll(Arb.numericDouble(min = -1e32, max = 1e32)) { x ->
            val expected = sin(x)
            val result = sine(BigDecimal(x))
            result.toDouble() shouldBe (expected plusOrMinus DEFAULT_ACCURACY.toDouble())
        }
    }
}