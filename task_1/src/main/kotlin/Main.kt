package com.sashka

import com.sashka.utils.CalcArctan.Companion.arctan

fun main(args: Array<String>) {
    require(args.isNotEmpty()) {"Provide an argument to a program"}

    val x: Double = args[0].toDouble()
    if (args.size > 1) {
        val n: Int = args[1].toInt()
        println(arctan(x, n))
        return
    }
    println(arctan(x, 10))
    return
}