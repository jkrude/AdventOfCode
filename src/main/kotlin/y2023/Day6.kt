package y2023

import common.extensions.productOf
import common.findAllLong
import common.readFileLines

object Day6 {

    private fun winningPossibilities(availableTime: Long, previousBest: Long): Int =
        (1L until availableTime).count { charging -> (availableTime - charging) * charging > previousBest }

    fun partOne(lines: List<String>): Int {
        val (times, distances) = lines.map { l -> l.findAllLong().toList() }
        return times.zip(distances).productOf { (time, distance) -> winningPossibilities(time, distance) }
    }

    fun partTwo(lines: List<String>): Int {
        val (time, distance) = lines.map { it.substringAfter(": ").replace(" ", "").toLong() }
        return winningPossibilities(time, distance)
    }
}

fun main() {
    val input = readFileLines(6, 2023)
    println(Day6.partOne(input))
    println(Day6.partTwo(input))
}