package domain

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.numericDouble
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.example.domain.Secant
import org.example.utils.DEFAULT_ACCURACY
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.math.PI
import kotlin.math.cos

class SecantTest {

    @Test
    fun edgeCasesTest() {
        val cases = listOf(
            Triple("Zero", 0.0, 1.0),
            Triple("Negative Zero", -0.0, 1.0),
            Triple("Pi", PI, -1.0),
            Triple("Negative Pi", -PI, -1.0),
            Triple("Two Pi", 2 * PI, 1.0),
            Triple("Pi/3", PI / 3, 2.0),
            Triple("Negative Pi/3", -PI / 3, 2.0)
        )

        val e = DEFAULT_ACCURACY

        for ((name, x, expected) in cases) {
            val result = Secant()(BigDecimal(x), e).toDouble()
            assertEquals(expected, result, DEFAULT_ACCURACY.toDouble(), "Failed on case: $name")
        }
    }

    @Test
    fun propertyTest() = runTest {
        val e = DEFAULT_ACCURACY.divide(BigDecimal(100000000))
        checkAll(Arb.numericDouble(min = -1e32, max = 1e32)) { x ->
            val expected = 1.0 / cos(x)
            val result = Secant()(BigDecimal(x), e)
            result.toDouble() shouldBe (expected plusOrMinus DEFAULT_ACCURACY.toDouble())
        }
    }
}