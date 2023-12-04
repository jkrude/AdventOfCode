package y2023

import common.extensions.findOverlapping
import common.extensions.map
import common.readFileLines

object Day1 {
    fun partOne(lines: List<String>): Int =
        lines.sumOf {
            (it.first { c -> c.isDigit() }.toString() + it.last { c -> c.isDigit() }).toInt()
        }


    fun partTwo(lines: List<String>): Int {
        val digits = "one, two, three, four, five, six, seven, eight, nine".split(", ")
        val digitToValue = digits.withIndex().associate { (index, digit) -> digit to index + 1 }

        fun String.asNumber() = (this.toIntOrNull() ?: digitToValue[this]!!).toString()

        val r = Regex("""(\d|${digits.joinToString("|")})""")
        return lines.sumOf { line ->
            val allMatches = r.findOverlapping(line)
            val (first, last) = (allMatches.first() to allMatches.last()).map { it.value.asNumber() }
            (first + last).toInt()
        }
    }
}

fun main() {
    val input = readFileLines(1, 2023)
    println(Day1.partOne(input))
    println(Day1.partTwo(input))
}