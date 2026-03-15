package domain.cosine

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.numericDouble
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.example.domain.Cosine
import org.example.domain.Sine
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.math.PI
import kotlin.math.cos

@Tag("unit")
@DisplayName("Cosine module tests")
class CosineModuleTest {

    private lateinit var sine: Sine
    private lateinit var cosine: Cosine

    @BeforeEach
    fun init() {
        sine = Sine()
        cosine = Cosine(sine)
    }

    @Test
    fun edgeCasesTest() {
        val cases = listOf(
            Triple("Zero", 0.0, 1.0),
            Triple("Negative Zero", -0.0, 1.0),
            Triple("Pi", PI, -1.0),
            Triple("Negative Pi", -PI, -1.0),
            Triple("Half Pi", PI / 2, 0.0),
            Triple("Negative Half Pi", -PI / 2, 0.0),
            Triple("Two Pi", 2 * PI, 1.0)
        )

        for ((name, x, expected) in cases) {
            val result = cosine(BigDecimal(x)).toDouble()
            Assertions.assertEquals(expected, result, DEFAULT_ACCURACY.toDouble(), "Failed on case: $name")
        }
    }

    @Test
    fun propertyTest() = runTest {
        checkAll(Arb.numericDouble(min = -1e32, max = 1e32)) { x ->
            val expected = cos(x)
            val result = cosine(BigDecimal(x))
            result.toDouble() shouldBe (expected plusOrMinus DEFAULT_ACCURACY.toDouble())
        }
    }
}