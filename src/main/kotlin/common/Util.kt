package common

import java.io.File
import kotlin.math.abs


fun readFileLines(day: Int, year: Int): List<String> = readFile(day, year).readLines()
fun readFile(day: Int, year: Int): File =
    File("src/main/resources/Y$year/day$day.txt")

fun readFileText(day: Int, year: Int): String = readFile(day, year).readText()

fun sumTo(x: Int) = if (x > 0) x * (x + 1) / 2 else abs(x) * (abs(x) + 1) / -2
