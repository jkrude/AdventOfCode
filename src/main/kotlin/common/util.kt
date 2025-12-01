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

val longRegex = """(-?\d+)""".toRegex()
fun String.findAllLong(startIndex: Int = 0): Sequence<Long> =
    longRegex.findAll(this, startIndex = startIndex).map { it.value.toLong() }

infix fun String.stringDifference(other: String): String =
    """
        $this
        $other
        ${"-".repeat(max(this.length, other.length))}
        ${this.zip(other).map { (a, b) -> if (a == b) '.' else 'X' }.joinToString("")}
    """.trimIndent()

fun Boolean.toInt(): Int = if (this) 1 else 0
fun Boolean.toLong(): Long = if (this) 1L else 0L
