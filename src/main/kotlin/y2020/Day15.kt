package y2020

import common.readFileLines

object Day15 {

    private fun solve(startNumbers: List<Int>, turns: Int): Int {
        // number -> last turn it was sayed
        val lastTimeSayed: MutableMap<Int, Int> =
            startNumbers
                .dropLast(1)
                .mapIndexed { index, i -> i to (index) }
                .toMap(HashMap())
        var lastSayed = startNumbers.last()
        repeat(turns - startNumbers.size) {

            val lastTurn = (it + startNumbers.size) - 1 // this turn - 1

            val nowSpoken = lastTimeSayed[lastSayed]
                ?.let { lty -> lastTurn - lty }
                ?: 0
            lastTimeSayed[lastSayed] = lastTurn
            lastSayed = nowSpoken
        }
        return lastSayed
    }

    private fun parseStartNumbers(lines: List<String>): List<Int> =
        lines.first()
            .split(",")
            .map { it.toInt() }

    fun partOne(lines: List<String>): Int =
        solve(parseStartNumbers(lines), 2020)

    fun partTwo(lines: List<String>): Int =
        solve(parseStartNumbers(lines), 30_000_000)
}

fun main() {
    println(Day15.partOne(readFileLines(15, 2020)))
    println(Day15.partTwo(readFileLines(15, 2020)))
}