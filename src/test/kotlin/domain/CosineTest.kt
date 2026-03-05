package domain

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.numericDouble
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.example.domain.Cosine
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.math.PI
import kotlin.math.cos

class CosineTest {

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
            val result = Cosine()(BigDecimal(x)).toDouble()
            assertEquals(expected, result, DEFAULT_ACCURACY.toDouble(), "Failed on case: $name")
        }
    }

    @Test
    fun propertyTest() = runTest {
        checkAll(Arb.numericDouble(min = -1e32, max = 1e32)) { x ->
            val expected = cos(x)
            val result = Cosine()(BigDecimal(x))
            result.toDouble() shouldBe (expected plusOrMinus DEFAULT_ACCURACY.toDouble())
        }
    }
}