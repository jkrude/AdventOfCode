package y2024

import common.algorithms.Search
import common.extensions.Idx2D
import common.extensions.List2D
import common.extensions.Lists2D.containsIndex
import common.extensions.Lists2D.get
import common.extensions.Lists2D.indices2d
import common.extensions.neighbours
import common.readFileLines

object Day10 {

    private fun List2D<Int>.countTrails(start: Idx2D, revisits: Boolean): Int {
        var encounteredNines = 0
        val search = Search
            .startingFrom(start)
            .neighbors { ij ->
                ij.neighbours()
                    .filter { n -> this.containsIndex(n) && this[n] == this[ij] + 1 }
            }
            .onEachVisit { if (this[it] == 9) encounteredNines += 1 }
        if (revisits) search.allowRevisit()
        search.executeDfs()
        return encounteredNines
    }

    fun allTrails(lines: List<String>, revisits: Boolean): Int {
        val grid = lines.map { it.map { c -> c.digitToInt() } }
        return grid.indices2d()
            .filter { ij: Idx2D -> grid[ij] == 0 }
            .sumOf { grid.countTrails(it, revisits) }
    }

    fun partOne(lines: List<String>): Int = allTrails(lines, false)

    fun partTwo(lines: List<String>): Int = allTrails(lines, true)
}

fun main() {
    val input = readFileLines(10, 2024)
    println(Day10.partOne(input))
    println(Day10.partTwo(input))
}