package y2020

import common.readFileLines

object Day5 {


    private fun seatId(line: String): Int {
        val row = line.take(7)
        val column = line.takeLast(3)
        return (row
            .replace("F", "0")
            .replace("B", "1")
            .toInt(2) * 8) +
                column
                    .replace("L", "0")
                    .replace("R", "1")
                    .toInt(2)
    }

    fun partOne(lines: List<String>): Int {
        return lines.maxOf { seatId(it) }

    }

    fun partTwo(lines: List<String>): Int {
        val ids = lines
            .map(::seatId)
            .sorted()
        return ids.withIndex()
            .first { (index, seatId) ->
                ids[index + 1] != seatId + 1
                        && ids.getOrNull(index - 8) != null
                        && ids.getOrNull(index + 8) != null

            }.value + 1 // Plus one as we find the gap between consecutive numbers
    }
}

fun main() {
    println(Day5.partOne(readFileLines(5, 2020)))
    println(Day5.partTwo(readFileLines(5, 2020)))
}