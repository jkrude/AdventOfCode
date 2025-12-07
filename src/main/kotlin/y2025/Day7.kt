package y2025

import common.algorithms.Search
import common.extensions.Idx2D
import common.extensions.Lists2D.columns
import common.extensions.Lists2D.containsIndex
import common.extensions.Lists2D.get
import common.extensions.Lists2D.indices2d
import common.readFileLines

object Day7 {

    fun partOne(lines: List<String>): Long {
        val grid = lines.map { it.toList() }.columns()
        val start: Idx2D = grid.indices2d().first { grid[it] == 'S' }
        var splits = 0L
        Search.SearchBuilder(listOf(start))
            .neighbors { (x, y) ->
                val next = if (grid[x][y] == '^') {
                    listOf(x - 1 to y, x + 1 to y) // split into left and right
                } else listOf(x to y + 1)
                next.filter { n -> grid.containsIndex(n) }
            }.onEachVisit { splits += if (grid[it] == '^') 1 else 0 }
            .executeBfs()
        return splits
    }

    fun partTwo(lines: List<String>): Long {
        val grid = lines.map { it.toList() }.columns()
        val start: Idx2D = grid.indices2d().first { grid[it] == 'S' }

        val cache = mutableMapOf<Idx2D, Long>()

        fun timelines(idx2D: Idx2D): Long {
            if (!grid.containsIndex(idx2D)) return 0L
            if (idx2D.second == grid.first().lastIndex) return 1L
            cache[idx2D]?.let { return it }
            val (x, y) = idx2D
            return if (grid[idx2D] == '^') {
                timelines(x - 1 to y) + timelines(x + 1 to y)
            } else {
                timelines(x to y + 1)
            }.also { cache[idx2D] = it }
        }
        return timelines(start)
    }
}

fun main() {
    val input = readFileLines(7, 2025)
    println(Day7.partOne(input))
    println(Day7.partTwo(input))
}