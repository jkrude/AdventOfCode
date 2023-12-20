package y2023

import arrow.core.curried
import common.*
import common.Direction.Companion.moveToward
import common.algorithms.Dijkstra
import common.extensions.List2D
import common.extensions.Lists2D.columns

object Day17 {

    data class Node(val position: Point2D, val direction: Direction, val count: Int)

    private fun solve(lines: List<String>, neighbors: (List2D<Int>, Node) -> Iterable<Pair<Int, Node>>): Int {
        val grid: List2D<Int> = lines.map { it.map { c -> c.digitToInt() } }.columns()
        val end = grid.lastIndex x2y grid.last().lastIndex

        return Dijkstra.dijkstra(
            listOf(Direction.EAST, Direction.SOUTH).map { Node(0 x2y 0, it, 0) },
            neighbors.curried().invoke(grid),
        ) { it.position == end }!!
    }

    fun partOne(lines: List<String>) = solve(lines) { grid, node ->
        Direction.entries.mapNotNull { d ->
            if (d == node.direction.opposite()
                || (d == node.direction && node.count == 3)
            ) null
            else Node(
                node.position.moveToward(d), d,
                if (d == node.direction) node.count + 1 else 1
            )
                .let { grid.getOrNull(it.position)?.to(it) }
        }
    }

    fun partTwo(lines: List<String>): Int {

        fun List2D<Int>.changeDirection(position: Point2D, direction: Direction): Pair<Point2D, Int>? {
            var weights = 0
            var nextPosition = position
            repeat(4) {
                nextPosition = nextPosition.moveToward(direction)
                if (this.getOrNull(nextPosition) == null) return null
                weights += this[nextPosition]
            }
            return nextPosition to weights
        }

        val neighbors: (List2D<Int>, Node) -> Iterable<Pair<Int, Node>> = { grid, node ->
            Direction.entries.mapNotNull { d ->
                if (d.ordinal == (node.direction.ordinal + 2) % 4) return@mapNotNull null
                if (d == node.direction && node.count == 10) return@mapNotNull null
                if (d == node.direction) {
                    val next = Node(node.position.moveToward(d), d, node.count + 1)
                    if (grid.getOrNull(next.position) == null) return@mapNotNull null
                    grid[next.position] to next
                } else {
                    val (nextPosition, weights) = grid.changeDirection(node.position, d) ?: return@mapNotNull null
                    val next = Node(nextPosition, d, 4)
                    weights to next
                }
            }
        }
        return solve(lines, neighbors)
    }

    fun visualize(path: List<Node>, map: List2D<Int>) =
        map.columns().withIndex().joinToString("\n") { (y, col) ->
            col.withIndex().joinToString("") { (x, i) ->
                val first = path.firstOrNull { it.position.x == x && it.position.y == y }
                first?.direction?.visualize()?.toString() ?: i.toString()
            }
        }
}

fun main() {
    val input = readFileLines(17, 2023)
    println(Day17.partOne(input))
    println(Day17.partTwo(input))
}