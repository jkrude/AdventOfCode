package y2020

import common.readFileText

object Day6 {

    private fun uniquelyAnsweredYes(text: String): Int {
        return text
            .replace("\n", "")
            .toSet()
            .size
    }

    fun partOne(lines: List<String>): Int =
        lines.sumOf { uniquelyAnsweredYes(it) }

    fun partTwo(lines: List<String>): Int =
        lines.sumOf { answers ->
            answers.split("\n")
                .map { it.toSet() }
                .reduce { a, b -> a intersect b }
                .size
        }
}

fun main() {
    println(Day6.partOne(readFileText(6, 2020).split("\n\n")))
    println(Day6.partTwo(readFileText(6, 2020).split("\n\n")))
}