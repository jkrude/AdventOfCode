package y2024

import common.findAllLong
import common.math.InplaceCounter
import common.readFileLines


object Day7 {

    private fun parse(lines: List<String>): List<Pair<Long, List<Long>>> =
        lines.map {
            it.findAllLong().toList().let { longs -> longs.first() to longs.drop(1) }
        }


    fun sumOfSatisfiable(lines: List<String>, operators: Int): Long =
        parse(lines)
            .filter { satisfiable(it.first, it.second, operators) }
            .sumOf { it.first }

    /**
     * Use a counter to enumerate all possible options of operator placements.
     * The number of available operators is the base of the counter.
     * We have parts.size-1 operator placement option which gives the number of counter-digits.
     */
    fun satisfiable(total: Long, parts: List<Long>, base: Int): Boolean =
        InplaceCounter(parts.lastIndex, base).asSequence().any { option: IntArray ->
            val assignmentsAndParts = option zip parts.drop(1)
            total == assignmentsAndParts.fold(parts.first()) { acc, (id, part) ->
                when (id) {
                    0 -> acc + part
                    1 -> acc * part
                    2 -> (acc.toString() + part.toString()).toLong()
                    else -> error("")
                }
            }
        }

    fun partOne(lines: List<String>): Long = sumOfSatisfiable(lines, operators = 2)

    fun partTwo(lines: List<String>): Long = sumOfSatisfiable(lines, operators = 3)
}

fun main() {
    val input = readFileLines(7, 2024)
    println(Day7.partOne(input))
    println(Day7.partTwo(input))
}