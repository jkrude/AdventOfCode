package y2022

import common.Direction
import common.Point2D
import common.algorithms.Search
import common.readFileLines
import common.x2y
import kotlin.math.min

object Day24 {

    private class SnowMap(
        private val blizzardPositions: List<Blizzard>,
        private val width: Int,
        private val height: Int,
        val start: Point2D,
        val end: Point2D
    ) {

        private fun Direction.visualize(): Char {
            return when (this) {
                Direction.WEST -> '<'
                Direction.EAST -> '>'
                Direction.NORTH -> '^'
                Direction.SOUTH -> 'v'
            }
        }

        fun visualize(): String {
            val map2D = Array(height) { Array(width) { '.' } }
            blizzardPositions.forEach {
                when (map2D[it.position.y][it.position.x]) {
                    '.' -> map2D[it.position.y][it.position.x] = it.direction.visualize()
                    '<', '>', '^', 'v' -> map2D[it.position.y][it.position.x] = '2'
                    else -> map2D[it.position.y][it.position.x] =
                        map2D[it.position.y][it.position.x].digitToInt().inc().toChar()
                }
            }
            return buildString {
                append("".padEnd(width + 2, '#'))
                append("\n")
                append(map2D.joinToString("\n") {
                    it.joinToString("", prefix = "#", postfix = "#")
                })
                append("\n")
                append("".padEnd(width + 2, '#'))
                append("\n")
            }
        }

        private fun moveBlizzard(blizzard: Blizzard): Blizzard {
            val movedBlizzard = blizzard.move()
            val nextPosition = movedBlizzard.position
            if (nextPosition.x < 0) nextPosition.x = (width - 1)
            if (nextPosition.x == width) nextPosition.x = 0
            if (nextPosition.y < 0) nextPosition.y = (height - 1)
            if (nextPosition.y == height) nextPosition.y = 0
            return movedBlizzard
        }

        fun next(iterations: Int = 1): SnowMap {
            require(iterations > 0)
            var nextMap = this.blizzardPositions.map { moveBlizzard(it) }
            repeat(iterations - 1) {
                nextMap = nextMap.map { moveBlizzard(it) }
            }
            return SnowMap(nextMap, width, height, start, end)
        }

        private fun inside(position: Point2D) =
            position == start || position == end ||
                    position.x in 0 until width && position.y in 0 until height

        operator fun List<Blizzard>.contains(point: Point2D) = this.any { it.position == point }

        fun freeNeighboursOf(position: Point2D): List<Point2D> {
            return (listOf(position) + position.fourNeighbours())
                .filter { (it !in blizzardPositions) && inside(it) }
        }
    }

    private data class Blizzard(
        val direction: Direction,
        val position: Point2D
    ) {
        fun move(): Blizzard {
            val nextPosition = when (this.direction) {
                Direction.NORTH -> position + (0 x2y -1)
                Direction.SOUTH -> position + (0 x2y 1)
                Direction.WEST -> position + (-1 x2y 0)
                Direction.EAST -> position + (1 x2y 0)
            }
            return Blizzard(direction, nextPosition)
        }
    }

    private fun parse(lines: List<String>): SnowMap {
        val snowMap2D = lines.drop(1).dropLast(1).flatMapIndexed { y, line: String -> // Ignore walls
            line.drop(1).dropLast(1) // Ignore walls
                .mapIndexedNotNull { x, char ->
                    when (char) {
                        '.' -> null
                        '<' -> Blizzard(Direction.WEST, x x2y y)
                        '>' -> Blizzard(Direction.EAST, x x2y y)
                        '^' -> Blizzard(Direction.NORTH, x x2y y)
                        'v' -> Blizzard(Direction.SOUTH, x x2y y)
                        else -> throw IllegalArgumentException(char.toString())
                    }
                }
        }
        // - 2 for walls on both sided
        return SnowMap(
            snowMap2D,
            lines.first().length - 2,
            lines.size - 2,
            0 x2y -1,
            lines.first().length - 3 x2y lines.size - 2
        )
    }

    class Searcher(lines: List<String>) {
        private val initialSnowMap = parse(lines)
        private val boardConfigurations: MutableMap<Int, SnowMap> = mutableMapOf(0 to initialSnowMap)
        val startPosition = initialSnowMap.start
        val finishPosition = initialSnowMap.end

        private fun getFreeNeighboursOrWait(
            minute: Int,
            position: Point2D
        ): List<Point2D> {
            // Find or generate next board configuration
            val nextMinute = minute + 1
            if ((nextMinute) in boardConfigurations) {
                return boardConfigurations[nextMinute]!!.freeNeighboursOf(position)
            }
            val previousBoardPosition = if (minute in boardConfigurations) {
                boardConfigurations[minute]!!
            } else {
                (boardConfigurations[0] ?: throw IllegalStateException("Initial configuration missing"))
                    .next(minute)
            }
            val nextMinutesConfiguration = previousBoardPosition.next()
            boardConfigurations[nextMinute] = nextMinutesConfiguration
            return nextMinutesConfiguration.freeNeighboursOf(position)
        }

        private fun searchOptions(
            minutePositionPair: Pair<Int, Point2D>,
            currentBest: Int,
            end: Point2D
        ): List<Pair<Int, Point2D>> {
            // Important: put current position as first option → dfs will queue it in last
            val (minute: Int, position: Point2D) = minutePositionPair
            if (position == end || minute > currentBest) return emptyList()
            return getFreeNeighboursOrWait(minute, position).map { minute + 1 to it }
        }

        fun search(from: Point2D, to: Point2D, startingMinute: Int, maxDepth: Int = 400): Int {
            var currentBest = startingMinute + maxDepth
            // Go to adjacent tile or stay
            Search.genericSearch(
                startingMinute to from,
                { searchOptions(it, currentBest, end = to) },
                Search::depthFirst,
                callOnEachVisited = { (minute, point) ->
                    if (point == to) currentBest = min(minute, currentBest)
                }
            )
            return currentBest
        }

    }

    fun partOne(lines: List<String>): Int {

        /**
         * Every state is clearly defined by
         *  position
         *  minute
         *
         * DFS through all options
         *  being: move (left, right, up, down, wait)
         * we don't know how long to wait
         * save how long it took you
         *  cancel dfs run if it took longer than current min
         */
        val searcher = Searcher(lines)
        return searcher.search(searcher.startPosition, searcher.finishPosition, 0)
    }

    fun partTwo(lines: List<String>): Int {
        with(Searcher(lines)) {
            val startToFinish = search(startPosition, finishPosition, 0)
            val finishToStart = search(finishPosition, startPosition, startToFinish)
            return search(startPosition, finishPosition, finishToStart)
        }
    }
}

fun main() {
    println(Day24.partOne(readFileLines(2, 20224)))
    println(Day24.partTwo(readFileLines(2, 20224)))
}