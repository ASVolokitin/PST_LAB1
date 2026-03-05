package com.sashka

class BinomialHeap {

    private data class Node(
        val top: Double,
        var rank: Int = 0,
        val children: MutableMap<Int, Node> = mutableMapOf()
    )

    private var root: Node? = null

    fun insert(value: Double) {
        val newNode = Node(value)
        root = merge(root, newNode)
    }

    fun top(): Double? = root?.top

    fun extractTop(): Double? {
        val value = root?.top ?: return null

        var newRoot: Node? = null
        root?.children?.values?.forEach { child ->
            newRoot = merge(newRoot, child)
        }
        root = newRoot

        return value
    }

    private fun merge(a: Node?, b: Node?): Node? {
        if (a == null) return b
        if (b == null) return a

        var first = a
        var second = b

        if (shouldBeSwapped(first.top, second.top)) {
            first = b
            second = a
        }

        if (first.children.containsKey(second.rank)) {
            val existingChild = first.children.remove(second.rank)
            val carried = merge(existingChild, second)
            return merge(first, carried)
        } else {
            first.children[second.rank] = second
        }

        first.rank = (first.children.keys.maxOrNull() ?: -1) + 1

        return first
    }

    private fun shouldBeSwapped(top1: Double, top2: Double): Boolean {
        if (top1.isNaN() && !top2.isNaN()) return true
        if (top2.isNaN() && !top1.isNaN()) return false

        return top2.compareTo(top1) < 0
    }
}