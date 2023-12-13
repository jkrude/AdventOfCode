package y2023

import common.findAllLong
import common.readFileLines

object Day9 {

    private fun diffs(seq: List<Long>, take: (List<Long>) -> Long, combine: Long.(Long) -> Long): Long {

        fun recursive(seq: List<Long>): Long {
            val diff = seq.windowed(2).map { (x, y) -> y - x }
            return if (diff.all { it == 0L }) 0L
            else take(diff).combine(recursive(diff))
        }
        return take(seq).combine(recursive(seq))

    }

    private fun solve(lines: List<String>, diffs: (longList: List<Long>) -> Long) =
        lines.map { it.findAllLong().toList() }.sumOf { diffs(it) }

    fun partOne(lines: List<String>): Long =
        solve(lines) { diffs(it, List<Long>::last, Long::plus) }


    fun partTwo(lines: List<String>): Long =
        solve(lines) { diffs(it, List<Long>::first, Long::minus) }
}

fun main() {
    val input = readFileLines(9, 2023)
    println(Day9.partOne(input))
    println(Day9.partTwo(input))
}