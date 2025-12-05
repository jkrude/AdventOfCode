package y2025

import common.readFileText
import kotlin.math.max

object Day5 {

    private fun parse(input: String): Pair<List<LongRange>, List<Long>> {
        val (rangesStr, ingredientStr) = input.split("\n\n")
        val ranges = rangesStr.lines().map { line ->
            val (from, to) = line.split("-").map { it.toLong() }
            from..to
        }
        val ingredients = ingredientStr.lines().map { it.toLong() }
        return ranges to ingredients
    }

    fun partOne(input: String): Long {
        val (ranges, ingredients) = parse(input)
        return ingredients.count {
            ranges.any { r -> it in r }
        }.toLong()
    }

    fun partTwo(input: String): Long {
        val ranges = parse(input).first.sortedBy { it.first }
        // Collect a list of disjoint ranges - merge overlapping ones.
        val mergedRanges = mutableListOf(ranges.first())

        for (current in ranges.drop(1)) {
            val last = mergedRanges.last()

            // Check if `current` overlaps with 'last'.
            // We use <= last.last + 1 to catch adjacent ranges (e.g., 1-5 and 6-10).
            if (current.first <= last.last) {
                // Determine the new end of the merged range.
                // In the case that current is subsumed by last, the newEnd is equal to the old end
                // and current is not processed further.
                val newEnd = max(last.last, current.last)
                mergedRanges[mergedRanges.lastIndex] = last.first..newEnd
            } else {
                // No overlap, start a new disjoint range.
                mergedRanges.add(current)
            }
        }

        // (end - start + 1) because both are inclusive.
        return mergedRanges.sumOf { it.last - it.first + 1 }
    }
}

fun main() {
    val input = readFileText(5, 2025)
    println(Day5.partOne(input))
    println(Day5.partTwo(input))
}