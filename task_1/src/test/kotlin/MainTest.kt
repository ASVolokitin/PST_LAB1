import com.sashka.main
import com.sashka.utils.CalcArctan.Companion.arctan
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.util.Random
import kotlin.test.assertEquals

class MainTest {

    private val standardOut = System.out
    private val outputStreamCaptor = ByteArrayOutputStream()
    private val DELTA = 1e-10

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outputStreamCaptor))
    }

    @AfterEach
    fun tearDown() {
        System.setOut(standardOut)
    }

    @Test
    fun shouldCalculateArctanWithDefaultIterations() {
        val x = Random().nextDouble()
        val expected = arctan(x)
        val args = arrayOf(x.toString())

        main(args)

        assertEquals(expected, outputStreamCaptor.toString().toDouble(), DELTA, "arctan($x) should be $expected")
    }

    @Test
    fun shouldCalculateArctanWithCustomIterations() {
        val x = Random().nextDouble()
        val iters = 200
        val expected = arctan(x, iters)
        val args = arrayOf(x.toString())


        main(args)

       assertEquals(expected, outputStreamCaptor.toString().toDouble(), DELTA,"arctan($x) should be $expected with $iters iterations")
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