package y2023

import common.extensions.Lists2D.rotatedClockwise
import common.extensions.toCharList2D
import common.readFileText

object Day13 {

    fun horizontalSplit(pattern: List<List<Char>>, searchSmudge: Boolean = false): Int? {
        val rotated = pattern.rotatedClockwise()
        val below = verticalSplit(rotated, searchSmudge) ?: return null
        return pattern.size - below
    }

    fun verticalSplit(pattern: List<List<Char>>, searchSmudge: Boolean = false): Int? {
        fun smudgeFound(column: Int): Boolean {
            var found = false
            for (row in pattern) {
                for (delta in 0..pattern.first().lastIndex) {
                    val left = row.getOrNull(column - delta) ?: break
                    val right = row.getOrNull(column + delta + 1) ?: break
                    if (left != right) {
                        if (!searchSmudge || found) return false
                        found = true
                    }
                }
            }
            return !searchSmudge || found
        }

        for (col in 0 until pattern.first().lastIndex) {
            if (smudgeFound(col)) return col + 1
        }
        return null
    }

    private fun solve(input: String, searchSmudge: Boolean): Int =
        input.split("\n\n")
            .map { it.lines().toCharList2D() }
            .sumOf { verticalSplit(it, searchSmudge) ?: (horizontalSplit(it, searchSmudge)!! * 100) }


    fun partOne(input: String): Int = solve(input, false)

    fun partTwo(input: String): Int = solve(input, true)
}

fun main() {
    val input = readFileText(13, 2023)
    println(Day13.partOne(input))
    println(Day13.partTwo(input))
}