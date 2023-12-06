package common

import java.io.File
import kotlin.math.max


fun readFileLines(day: Int, year: Int): List<String> = readFile(day, year).readLines()
fun readFile(day: Int, year: Int): File =
    File("src/main/resources/Y$year/day$day.txt")

fun readFileText(day: Int, year: Int): String = readFile(day, year).readText()

fun maxNullable(left: Int?, right: Int?) = when {
    left == null && right != null -> right
    left != null && right == null -> left
    left != null && right != null -> max(left, right)
    else -> throw IllegalArgumentException("Both left and right where null")
}

val longRegex = """(\d+)""".toRegex()
fun String.findAllLong(startIndex: Int = 0): Sequence<Long> =
    longRegex.findAll(this, startIndex = startIndex).map { it.value.toLong() }