package org.example.hh.utils

import java.util.Properties

object ConfigReader {
    private val properties = Properties()

    init {
        val loader = Thread.currentThread().contextClassLoader
        loader.getResourceAsStream("config.properties").use { inputStream ->
            properties.load(inputStream)
        }
    }

    fun getProperty(key: String): String {
        return properties.getProperty(key) ?: throw RuntimeException("Ключ $key не найден в config.properties")
    }

    fun getPropertyOrDefault(key: String, defaultValue: String): String {
        return properties.getProperty(key) ?: defaultValue
    }
}
