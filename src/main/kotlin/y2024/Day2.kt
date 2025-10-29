package y2024

import common.findAllLong
import common.readFileLines

object Day2 {

    private fun isSafe(levels: List<Long>): Boolean {
        val increasing = levels[0] < levels[1]
        return levels.windowed(2).all { (l1, l2) ->
            val diff = if (increasing) l2 - l1 else l1 - l2
            diff in 1..3
        }
    }

    fun partOne(lines: List<String>): Int {
        val reports = lines.map { line -> line.findAllLong().toList() }
        return reports.count(::isSafe)
    }

    fun partTwo(lines: List<String>): Int {
        val reports = lines.map { line -> line.findAllLong().toList() }
        return reports.count { levels -> // runtime O(n^2) but with size of input acceptable.

            isSafe(levels) || levels.indices.any { i -> isSafe(levels.filterIndexed { j, _ -> j != i }) }
        }
    }
}

fun main() {
    val input = readFileLines(2, 2024)
    println(Day2.partOne(input))
    println(Day2.partTwo(input))
}