import com.sashka.BinomialHeap
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

class BinomialHeapTest {

    @Test
    fun shouldReturnEmptyHeap() {
        val heap = BinomialHeap()

        assertNull(heap.top(), "Top should be null for empty heap")
        assertNull(heap.extractTop(), "ExtractTop should be null for empty heap")
    }

    @Test
    fun shouldHandleSingleNode() {
        val heap = BinomialHeap()
        val value = Random.nextDouble()

        heap.insert(value)

        assertEquals(value, heap.top(), "Top should return the inserted value")
        assertEquals(value, heap.extractTop(), "ExtractTop should return the inserted value")
        assertNull(heap.top(), "Heap should be empty after extraction")
    }

    @Test
    fun shouldSortElements() {
        val heap = BinomialHeap()
        val inputValues = (1..100_000).map { Random.nextDouble() }.shuffled()

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