package y2024

import common.*
import common.Direction.Companion.moveToward
import common.algorithms.Dijkstra.allShortestDijkstra
import common.algorithms.Dijkstra.dijkstra
import common.extensions.List2D
import common.extensions.Lists2D.columns
import common.extensions.Lists2D.get
import common.extensions.Lists2D.indices2d
import common.extensions.toCharList2D

object Day16 {

    private fun List2D<Char>.validMoves(current: Pair<Point2D, Direction>): List<Pair<Int, Pair<Point2D, Direction>>> {
        val (pos, direction) = current
        val moves = mutableListOf(
            1000 to (pos to direction.turnClockwise()),
            1000 to (pos to direction.turnCounterClockwise()),
        )
        val nextPosition = pos.moveToward(direction)
        if (this.getOrNull(nextPosition)
                .let { c -> c == '.' || c == 'E' }
        ) {
            moves.add(1 to (pos.moveToward(direction) to direction))
        }
        return moves
    }

    private fun setup(input: String): Pair<List<List<Char>>, Pair<Point2D, Direction>> {
        val maze = input.lines().toCharList2D().columns()
        val start = Point2D.of(maze.indices2d().first { maze[it] == 'S' }) to Direction.EAST
        return maze to start
    }

    fun partOne(input: String): Long {
        val (maze, start) = setup(input)
        return dijkstra(
            startPoints = listOf(start),
            neighbors = { maze.validMoves(it) },
            isTarget = { maze[it.first] == 'E' }
        )!!.toLong()
    }

    fun partTwo(input: String): Long {
        val (maze, start) = setup(input)
        val (allPredecessor, _) = allShortestDijkstra(
            startPoints = listOf(start),
            neighbors = { maze.validMoves(it) },
            isTarget = { maze[it.first] == 'E' }
        ) ?: error("Could not find target")
        return allPredecessor.mapTo(mutableSetOf(), { it.first }).size.toLong()
    }
}

fun main() {
    val input = readFileText(16, 2024)
    println(Day16.partOne(input))
    println(Day16.partTwo(input))
}