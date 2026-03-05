import com.sashka.BinomialHeap
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BinomialHeapTest {

    @Test
    fun shouldReturnEmptyHeap() {
        val heap = BinomialHeap()

        assertNull(heap.top(), "Top should be null for empty heap")
        assertNull(heap.extractTop(), "ExtractTop should be null for empty heap")
    }

    @Test
    fun shouldHandleSingleNode() = runTest {

        checkAll<Double> { value ->
            val heap = BinomialHeap()

            heap.insert(value)

            assertEquals(value, heap.top(), "Top should return the inserted value")
            assertEquals(value, heap.extractTop(), "ExtractTop should return the inserted value")
            assertNull(heap.top(), "Heap should be empty after extraction")
        }
    }

    @Test
    fun shouldSortElements() = runTest {

        checkAll (Arb.list(Arb.double(), range = 0..1000)) { inputValues ->
            val heap = BinomialHeap()

            inputValues.forEach { heap.insert(it) }

            val sortedInput = inputValues.sorted()
            val extractedValues = mutableListOf<Double>()

            repeat(inputValues.size) {
                val extracted = heap.extractTop()
                assertNotNull(extracted)
                extractedValues.add(extracted!!)
            }

            assertEquals(sortedInput, extractedValues, "Elements should be extracted in sorted order")
            assertNull(heap.top(), "Heap should be empty after extracting all elements")
        }
    }
}