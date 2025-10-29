package y2024

import common.*
import common.Direction.Companion.moveToward
import common.extensions.Idx2D
import common.extensions.List2D
import common.extensions.Lists2D.columns
import common.extensions.Lists2D.get
import common.extensions.Lists2D.indices2d
import common.extensions.toCharList2D


object Day6 {

    private data class Guard(val pos: Point2D, val direction: Direction)

    private fun List2D<Char>.movementSequence(start: Idx2D) =
        generateSequence(Guard(Point2D.of(start), Direction.NORTH)) { (pos, direction) ->
            pos.moveToward(direction).let { next ->
                when {
                    !this.containsPoint(next) -> null
                    this[next] == '#' -> Guard(pos, direction.turnClockwise())
                    else -> Guard(next, direction)
                }
            }
        }

    fun partOne(lines: List<String>): Int {
        val grid: List<List<Char>> = lines.toCharList2D().columns()
        val start: Idx2D = grid.indices2d().find { grid[it] == '^' } ?: error("Expected to find '^'")
        return grid.movementSequence(start).map { it.pos }.toSet().size
    }

    private fun checkLoop(guard: Guard, grid: List2D<Char>, newBlock: Point2D): Boolean {
        val visited = mutableSetOf<Guard>()
        var movingGuard = guard
        while (true) {
            visited.add(movingGuard)
            val (pos, direction) = movingGuard
            pos.moveToward(direction).let { next ->
                movingGuard = when {
                    !grid.containsPoint(next) -> return false
                    grid[next] == '#' || next == newBlock -> Guard(pos, direction.turnClockwise())
                    else -> Guard(next, direction)
                }
            }
            if (movingGuard in visited) return true
        }
    }

    fun partTwo(lines: List<String>): Int {
        val grid: List<List<Char>> = lines.toCharList2D().columns()
        val start: Idx2D = grid.indices2d().find { grid[it] == '^' } ?: error("Expected to find '^'")
        val initialGuard = Guard(Point2D.of(start), Direction.NORTH)
        val blockPlacements = mutableSetOf<Point2D>()
        // From each point the guard will visit check if placing a block there would lead to a cycle.
        // We have to start the cycle search from the beginning as the block might influence the path up to here.
        grid.movementSequence(start)
            .drop(1).forEach { next ->
                if (next.pos !in blockPlacements && next.pos != initialGuard.pos) {
                    if (checkLoop(initialGuard, grid, next.pos)) {
                        blockPlacements.add(next.pos)
                    }
                }
            }
        return blockPlacements.size
    }

}

fun main() {
    val input = readFileLines(6, 2024)
    println(Day6.partOne(input))
    println(Day6.partTwo(input))
}