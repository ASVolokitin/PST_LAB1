package domain.secant

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.shouldBe
import org.example.domain.Cosine
import org.example.domain.Secant
import org.example.domain.Sine
import java.math.BigDecimal

class SecantStringTest : StringSpec({
    "secant of 0 should be 1" {
        val sine = Sine()
        val cosine = Cosine(sine)
        val secant = Secant(cosine)
        secant(BigDecimal.ZERO, BigDecimal("0.0001")) shouldBeEqualComparingTo BigDecimal.ONE
    }
})

class SecantBehaviorTest : BehaviorSpec({
    given("a secant function") {
        val sine = Sine()
        val cosine = Cosine(sine)
        val secant = Secant(cosine)
        `when`("calculating secant of pi") {
            val x = BigDecimal(Math.PI)
            val result = secant(x, BigDecimal("0.0001"))
            then("the result should be -1") {
                result.toBigInteger().toInt() shouldBe -1
            }
        }
    }
})

class SecantFunTest : FunSpec({
    test("secant of 2 * pi should be 1") {
        val sine = Sine()
        val cosine = Cosine(sine)
        val secant = Secant(cosine)
        val x = BigDecimal(2 * Math.PI)
        val result = secant(x, BigDecimal("0.0001"))
        result.toBigInteger().toInt() shouldBe 1
    }
})
