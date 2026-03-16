package org.example.utils

import org.example.domain.MathFunction
import org.knowm.xchart.XYChartBuilder
import org.knowm.xchart.BitmapEncoder
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Plotter(
    private val function: MathFunction,
    private val x1: BigDecimal,
    private val x2: BigDecimal,
    private val steps: Int,
    private val accuracy: BigDecimal
) {
    private val outputDir = Paths.get("plot_output").apply { Files.createDirectories(this) }

    fun plot(): String {
        val xData = mutableListOf<Double>()
        val yData = mutableListOf<Double>()
        val dx = (x2 - x1) / (BigDecimal(steps - 1))
        var x = x1
        for (i in 0 until steps) {
            xData.add(x.toDouble())
            yData.add(function(x, accuracy).toDouble())
            x += dx
        }
        val chart = XYChartBuilder()
            .width(800)
            .height(600)
            .title(function.functionName)
            .xAxisTitle("x")
            .yAxisTitle("y")
            .build()
        chart.addSeries(function.functionName, xData, yData)
        val fileName = "${function.functionName}_${LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm:ss"))}.png"
        val filePath = outputDir.resolve(fileName).toString()
        BitmapEncoder.saveBitmap(chart, filePath, BitmapEncoder.BitmapFormat.PNG)
        return filePath
    }
}