package com.sashka.util


import com.sashka.commands.Executable
import io.github.kelvindev15.kotlin2plantuml.plantuml.ClassDiagram
import io.github.kelvindev15.kotlin2plantuml.plantuml.Configuration
import java.io.File
import kotlin.reflect.KClass

fun main() {
    val classDirs = listOf(
        File("build/classes/kotlin/main")
    ).filter { it.exists() }

    if (classDirs.isEmpty()) {
        println("❌ Скомпилированные классы не найдены. Сначала соберите проект.")
        return
    }

    // 2. Собираем все .class файлы и преобразуем их в объекты KClass
    val classes: List<KClass<*>> = classDirs.flatMap { dir ->
        dir.walkTopDown()
            .filter { it.isFile && it.extension == "class" }
            // Игнорируем вложенные и анонимные классы (чтобы не засорять диаграмму)
            .filter { !it.name.contains("$") }
            .mapNotNull { file ->
                // Преобразуем путь файла в имя пакета (com/example/MyClass.class -> com.example.MyClass)
                val className = file.relativeTo(dir).path
                    .replace(File.separatorChar, '.')
                    .removeSuffix(".class")
                try {
                    Class.forName(className).kotlin
                } catch (e: Exception) {
                    null // Пропускаем классы, которые не удалось загрузить
                }
            }
    }

    if (classes.isEmpty()) {
        println("❌ В директории main не найдено ни одного класса!")
        return
    }

    // 3. Передаем весь массив классов в библиотеку (через spread-оператор *)
    val myPlantUML = ClassDiagram(
        *classes.toTypedArray(),
        configuration = Configuration(
            recurse = true
        )
    ).plantUml()

    // 4. Сохраняем результат
    val outputFile = File("build/reports/diagram.plantuml")
    outputFile.parentFile.mkdirs()
    outputFile.writeText(myPlantUML)

    println("✅ UML диаграмма успешно сгенерирована для ${classes.size} классов!")
    println("📁 Сохранено в: ${outputFile.absolutePath}")

//    val uml = ClassDiagram(Executable::class, configuration = Configuration(recurse = true))
}