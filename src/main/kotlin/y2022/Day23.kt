package y2022

import common.Point2D
import common.onlyUnique
import common.readFileLines
import common.x2y

object Day23 {

    private enum class Direction {
        NORTH,
        SOUTH,
        WEST,
        EAST
    }

    private fun smallestRectangleCover(positions: Set<Point2D>): Pair<IntRange, IntRange> {
        val minX = positions.minOf { it.x }
        val maxX = positions.maxOf { it.x }
        val minY = positions.minOf { it.y }
        val maxY = positions.maxOf { it.y }
        return minX..maxX to minY..maxY
    }

    private fun emptyFields(positions: Set<Point2D>): Int {
        val (xRange, yRange) = smallestRectangleCover(positions)
        return xRange.sumOf { x ->
            yRange.count { y -> (x x2y y) !in positions }
        }
    }

    fun visualize(positions: Set<Point2D>, padding: Int = 0) {
        var (xRange, yRange) = smallestRectangleCover(positions)
        yRange = (yRange.first - padding)..(yRange.last + padding)
        xRange = (xRange.first - padding)..(xRange.last + padding)
        yRange.forEach { y ->
            xRange.forEach { x ->
                if (x x2y y in positions) print("#") else print(".")
            }
            println()
        }
    }

    fun extractPositions(lines: List<String>): Set<Point2D> {
        return lines.withIndex().flatMap { (y, line) ->
            line.withIndex().mapNotNull { (x, char) ->
                if (char == '#') x x2y y else null
            }
        }.toSet()
    }

    private fun proposedMove(
        elf: Point2D,
        proposedDirections: List<Direction>,
        elfPositions: Set<Point2D>
    ): Point2D {
        if (!elf.wantsToMove(elfPositions)) return elf
        for (direction in proposedDirections) {
            val options = when (direction) {
                Direction.NORTH -> listOf(0 x2y -1, -1 x2y -1, 1 x2y -1)
                Direction.SOUTH -> listOf(0 x2y 1, -1 x2y 1, 1 x2y 1)
                Direction.WEST -> listOf(-1 x2y 0, -1 x2y -1, -1 x2y 1)
                Direction.EAST -> listOf(1 x2y 0, 1 x2y -1, 1 x2y 1)
            }
            if (options.map { elf + it }.none { it in elfPositions }) {
                return (elf + options.first())
            }
        }
        return elf
    }

    private fun Point2D.wantsToMove(elfPositions: Set<Point2D>) =
        this.eightNeighbours().any {
            it in elfPositions
        }

    private fun moveElves(elfPositions: Set<Point2D>, proposedDirections: MutableList<Direction>): Set<Point2D> {
        val proposedMoves = elfPositions.associateWith {
            proposedMove(it, proposedDirections, elfPositions)
        }
        val nextPositions = elfPositions.toMutableSet()

        val proposedMovesWithoutConflict: Set<Point2D> = proposedMoves.values.onlyUnique()
        proposedMoves.forEach { (lastPosition, nextPosition) ->
            if (nextPosition in proposedMovesWithoutConflict) {
                nextPositions.remove(lastPosition)
                nextPositions.add(nextPosition)
            }
        }
        val direction = proposedDirections.removeFirst()
        proposedDirections.add(direction)
        return nextPositions
    }

    fun partOne(lines: List<String>): Int {

        val proposedDirections = Direction.values().toMutableList()
        var elfPositions: Set<Point2D> = extractPositions(lines)

        repeat(10) {
            elfPositions = moveElves(elfPositions, proposedDirections)
        }
        return emptyFields(elfPositions)
    }

    fun partTwo(lines: List<String>): Int {
        val proposedDirections = Direction.values().toMutableList()
        var elfPositions: Set<Point2D> = extractPositions(lines)
        var counter = 1
        while (elfPositions.any { it.wantsToMove(elfPositions) }) {
            elfPositions = moveElves(elfPositions, proposedDirections)
            counter++
        }
        return counter
    }

}

fun main() {
    println(Day23.partOne(readFileLines(23)))
    println(Day23.partTwo(readFileLines(23)))
}