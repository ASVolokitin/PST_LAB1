import com.sashka.main
import com.sashka.utils.CalcArctan.Companion.arctan
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

class MainTest {

    private val standardOut = System.out
    private val outputStreamCaptor = ByteArrayOutputStream()
    private val DELTA = 1e-9

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outputStreamCaptor))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(standardOut)
    }

    @Test
    fun shouldCalculateArctanWithDefaultIterations() = runTest {

        checkAll<Double> { x ->
            outputStreamCaptor.reset()
            val expected = arctan(x)
            val args = arrayOf(x.toString())

            main(args)
            val actual = outputStreamCaptor.toString().trim().toDouble()

            assertEquals(expected, actual, DELTA, "arctan($x) should be $expected")
        }
    }

    @Test
    fun shouldCalculateArctanWithCustomIterations() = runTest {

        checkAll<Double> { x ->
            outputStreamCaptor.reset()
            val iterations = 200
            val expected = arctan(x, iterations)
            val args = arrayOf(x.toString())

            main(args)
            val actual = outputStreamCaptor.toString().trim().toDouble()

            assertEquals(expected, actual, DELTA,"arctan($x) should be $expected with $iterations iterations")
        }
    }

    @Test
    fun shouldThrowExceptionWhenNoArguments() {
        val args = emptyArray<String>()

        val exception = assertThrows<IllegalArgumentException> {
            main(args)
        }

        assertEquals("Provide an argument to a program", exception.message, "Should throw exception when no arguments")
    }

    @Test
    fun shouldThrowExceptionWhenArgumentIsNotNumber() {
        val args = arrayOf("blabla")

        val exception = assertThrows<NumberFormatException> {
            main(args)
        }
    }

    @Test
    fun shouldThrowExceptionWhenSecondArgumentIsNotNumber() {
        val args = arrayOf("1.0", "blabla")

        val exception = assertThrows<NumberFormatException> {
            main(args)
        }
    }

    @Test
    fun shouldHandleZeroIterations() {
        val args = arrayOf("0.5", "0")

        main(args)

        assertEquals("0.5\n", outputStreamCaptor.toString(), "With n=0 should output x")
    }
}