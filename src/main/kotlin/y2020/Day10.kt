package y2020

import common.plusElement
import common.readFileLines
import kotlin.math.abs

object Day10 {

    private fun allAdapterSorted(lines: List<String>): List<Int> {
        return lines.map(String::toInt)
            .plusElement { it.max() + 3 }
            .plusElement(0)
            .sorted()
    }

    // Find a path from the wall to the default adapter that visits all adapter.
    // As we have to use all adapter going from one adapter to the next higher one is always right.
    fun partOne(lines: List<String>): Int {
        val windowed = allAdapterSorted(lines) // Add default adapter and wall outlet
            .sorted()
            .windowed(2)
        val ones = windowed.count { (x, y) -> abs(x - y) == 1 }
        val threes = windowed.count { (x, y) -> abs(x - y) == 3 }
        return ones * threes
    }

    // Count the number of ways the wall can be connected to the adapter
    // You don't have to use all adapter now
    // Between two adapters there still can only be a difference of 1-3
    fun partTwo(lines: List<String>): Long {
        val allAdapter = allAdapterSorted(lines)

        // Dynamic programming starting from the highest going down to wall
        val nbrOfPathsFrom: MutableMap<Int, Long> = allAdapter.associateWith { 0L }.toMutableMap()
        nbrOfPathsFrom[nbrOfPathsFrom.keys.max()] = 1L
        for (adapter in allAdapter.reversed().drop(1)) { // The default adapter has only one
            nbrOfPathsFrom[adapter] =
                (1..3)
                    .map { adapter + it }
                    .sumOf { higherAdapter ->
                        nbrOfPathsFrom.getOrDefault(higherAdapter, 0L)
                    }
        }
        return nbrOfPathsFrom[0]!!
    }
}

fun main() {
    println(Day10.partOne(readFileLines(10, 2020)))
    println(Day10.partTwo(readFileLines(10, 2020)))
}