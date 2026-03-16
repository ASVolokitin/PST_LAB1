package org.example.utils

import org.example.domain.MathFunction
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.file.Files
import java.nio.file.Paths

class CsvWriter(
    private val function: MathFunction,
    private val x1: BigDecimal,
    private val x2: BigDecimal,
    private val steps: Int,
    private val accuracy: BigDecimal
) {
    private val outputDir = Paths.get("src/test/resources").apply { Files.createDirectories(this) }

    fun write(): File {
        val name = function.functionName.replace("(", "_").replace(")", "")
        val fileName = "${name}_data.csv"
        val file = outputDir.resolve(fileName).toFile()
        file.bufferedWriter().use { writer ->
            writer.appendLine("x,expected_${name}")
            val dx = if (steps > 1) (x2 - x1) / (BigDecimal(steps - 1)) else BigDecimal.ZERO
            var x = x1
            for (i in 0 until steps) {
                val y = function(x, accuracy)
                writer.appendLine("${x},${y.setScale(DEFAULT_ACCURACY.scale(), RoundingMode.HALF_EVEN)}")
                x += dx
            }
        }
        return file
    }
}