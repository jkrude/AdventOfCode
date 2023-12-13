package y2023

import common.extensions.Idx2D
import common.extensions.Lists2D.columns
import common.extensions.Lists2D.get
import common.extensions.Lists2D.indices2d
import common.extensions.autoRange
import common.extensions.manhattenTo
import common.extensions.toCharList2D
import common.readFileLines

object Day11 {

    fun solve(lines: List<String>, expansionFactor: Long): Long {
        val map = lines.toCharList2D()
        val columns = map.columns()
        val emptyRowIds: List<Int> = lines.indices.filter { lines[it].all { c -> c == '.' } }
        val emptyColumnIds = columns.indices.filter { columns[it].all { c -> c == '.' } }

        fun List<Int>.countWithin(start: Int, end: Int) = (start autoRange end).let { range -> count { it in range } }

        fun Idx2D.distanceTo(other: Idx2D, expansionFactor: Long) =
            this.manhattenTo(other) +
                    (emptyRowIds.countWithin(this.first, other.first) +
                            emptyColumnIds.countWithin(this.second, other.second)) * expansionFactor

        val galaxies = map.indices2d().filter { map[it] == '#' }
        return galaxies
            // Create all pairs of galaxies.
            .flatMapIndexed { index: Int, g -> galaxies.subList(index + 1, galaxies.size).map { g to it } }
            // Calculate their distances.
            .sumOf { (from, to) -> from.distanceTo(to, expansionFactor) }
    }


    fun partOne(lines: List<String>) = solve(lines, 1L)

    fun partTwo(lines: List<String>) = solve(lines, 1_000_000L - 1L)
}

fun main() {
    val input = readFileLines(11, 2023)
    println(Day11.partOne(input))
    println(Day11.partTwo(input))
}