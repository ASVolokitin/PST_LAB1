package org.example.utils

import org.example.domain.MathFunction
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CsvWriter(
    private val function: MathFunction,
    private val x1: BigDecimal,
    private val x2: BigDecimal,
    private val steps: Int,
    private val accuracy: BigDecimal
) {
    private val outputDir = Paths.get("csv_output").apply { Files.createDirectories(this) }

    fun write(): File {
        val name = function.functionName
        val fileName = "${name}_${LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss"))}.csv"
        val file = outputDir.resolve(fileName).toFile()
        file.bufferedWriter().use { writer ->
            writer.appendLine("X,Y")
            val scale = 20
            val dx = (x2 - x1) / (BigDecimal(steps - 1))
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