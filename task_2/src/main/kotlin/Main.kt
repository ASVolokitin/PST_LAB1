import java.util.Random


fun main() {
    val heap = BinomialHeap()

    for (i in 0..1000) {
        heap.insert(Random().nextDouble() * 100)
    }

    println("Min: ${heap.top()}")
    println("Extract min: ${heap.extractTop()}")
    println("New min: ${heap.top()}")
}