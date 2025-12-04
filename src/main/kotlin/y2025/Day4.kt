package y2025

import common.extensions.Idx2D
import common.extensions.List2D
import common.extensions.Lists2D.containsIndex
import common.extensions.Lists2D.get
import common.extensions.Lists2D.getOrNull
import common.extensions.Lists2D.indices2d
import common.extensions.Lists2D.set
import common.extensions.allNeighbour
import common.readFileLines

object Day4 {

    /**
     * A position can be removed if it is a roll of paper (@)
     * and has less than four neighboring rolls of paper.
     */
    private fun List2D<Char>.canBeRemoved(idx2D: Idx2D) =
        this[idx2D] == '@' &&
                idx2D.allNeighbour().count { n ->
                    this.getOrNull(n) == '@'
                } < 4

    fun partOne(lines: List<String>): Long =
        lines.map { it.toList() }.let { grid ->
            grid.indices2d().count { grid.canBeRemoved(it) }.toLong()
        }

    fun partTwo(lines: List<String>): Long {
        val grid = lines.mapTo(mutableListOf()) { it.toMutableList() }

        var candidates: Collection<Idx2D> = grid.indices2d().filter { grid.canBeRemoved(it) }
        var removed = 0L
        // Iterate until no more paper can be removed
        while (candidates.isNotEmpty()) {
            removed += candidates.size
            candidates.forEach { grid[it] = '.' } // remove rolls of paper
            // Check neighbors of removed items whether they can be removed now.
            candidates = candidates.flatMapTo(mutableSetOf()) { candidate ->
                candidate.allNeighbour()
                    .filter { grid.containsIndex(it) && grid.canBeRemoved(it) }
            }
        }
        return removed
    }
}

fun main() {
    val input = readFileLines(4, 2025)
    println(Day4.partOne(input))
    println(Day4.partTwo(input))
}