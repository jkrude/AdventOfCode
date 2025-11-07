package y2024

import common.Direction
import common.Direction.Companion.moveToward
import common.Point2D
import common.algorithms.Search
import common.extensions.Idx2D
import common.extensions.Lists2D.columns
import common.extensions.Lists2D.get
import common.extensions.Lists2D.indices2d
import common.extensions.Lists2D.set
import common.extensions.toCharList2D
import common.get
import common.readFileText

object Day15 {

    private fun parse(input: String): Pair<MutableList<MutableList<Char>>, List<Direction>> {
        val (gridBlock, instructionsBlock) = input.split("\n\n")
        val grid = gridBlock.lines()
            .toCharList2D()
            .columns()
            .mapTo(ArrayList()) { it.toMutableList() }
        val instructions: List<Direction> = instructionsBlock
            .replace("\n", "")
            .map(Direction.Companion::ofArrow)
        return grid to instructions
    }

    // First is x coordinate second is y
    private fun Idx2D.GPS(): Long = this.first + this.second * 100L

    fun partOne(input: String): Long {
        val (grid, instructions) = parse(input)
        var robot: Point2D = Point2D.of(grid.indices2d().first { ij: Idx2D -> grid[ij] == '@' })
        grid[robot] = '.'
        for (direction in instructions) {
            val immediateNext = robot.moveToward(direction)
            var next = immediateNext
            while (grid[next] != '.' && grid[next] != '#') next = next.moveToward(direction)
            if (grid[next] == '#') continue // move not possible
            if (grid[immediateNext] == 'O') { // we have to move Os
                grid[next] = 'O'
                grid[immediateNext] = '.'
            }
            robot = immediateNext
        }
        return grid.indices2d().filter { grid[it] == 'O' }.sumOf { it.GPS() }
    }


    fun MutableList<MutableList<Char>>.adjacentMoveableBoxes(
        immediateNext: Point2D,
        pushDirection: Direction,
    ): Iterable<Point2D>? {
        /**
         * Compute all box positions that would be affecting by pushing towards a certain direction.
         * If the boxes could not be moved, the function returns null.
         */

        var encounteredWall = false
        val positions: MutableList<Point2D> = mutableListOf()
        Search.SearchBuilder(listOf(immediateNext))
            .neighbors { position: Point2D ->
                when (this[position]) {
                    '[' -> listOf(
                        position.moveToward(pushDirection),
                        position.moveToward(Direction.EAST),
                    )

                    ']' -> listOf(
                        position.moveToward(pushDirection),
                        position.moveToward(Direction.WEST)
                    )

                    '#' -> emptyList<Point2D>().also { encounteredWall = true }
                    else -> emptyList()
                }
            }
            .onEachVisit { if (this[it] == '[' || this[it] == ']') positions.add(it) }
            .executeBfs()
        return if (encounteredWall) null else positions
    }

    fun partTwo(input: String): Long {
        val (smallGrid, instructions) = parse(input)
        // x/i is left and right | y/j is up and down
        val grid = MutableList(smallGrid.size * 2) { MutableList(smallGrid[0].size) { '.' } }

        for ((x, y) in smallGrid.indices2d()) {
            when (smallGrid[x][y]) {
                'O' -> {
                    grid[x * 2][y] = '['
                    grid[x * 2 + 1][y] = ']'
                }

                '#' -> {
                    grid[x * 2][y] = '#'
                    grid[x * 2 + 1][y] = '#'
                }

                '@' -> grid[x * 2][y] = '@'
            }
        }
        var robot: Point2D = Point2D.of(grid.indices2d().first { ij: Idx2D -> grid[ij] == '@' })
        for (direction in instructions) {
            val immediateNext = robot.moveToward(direction)
            if (grid[immediateNext] == '#') continue
            if (grid[immediateNext] == '[' || grid[immediateNext] == ']') {
                val nexts = grid.adjacentMoveableBoxes(immediateNext, pushDirection = direction) ?: continue
                // The next positions are ordered by BFS search, meaning we start with the current position first and adding the outer positions last.
                // We need to iterate in reverse to avoid overriding.
                // The outer positions can be safely moved as they are guaranteed to have free space in the push direction.
                for (boxPosition in nexts.reversed()) {
                    require(grid[boxPosition] == '[' || grid[boxPosition] == ']')
                    grid[boxPosition.moveToward(direction)] = grid[boxPosition]
                    grid[boxPosition] = '.'
                }
            }
            require(grid[immediateNext] == '.')
            grid[robot] = '.' // mark robot position for debugging
            grid[immediateNext] = '@'
            robot = immediateNext
        }
        // This was super misleading. I thought you have to check whether [ is closer to the left edge than ] is to the right edge.
        return grid.indices2d().filter { grid[it] == '[' }.sumOf { it.GPS() }
    }
}

fun main() {
    val input = readFileText(15, 2024)
    println(Day15.partOne(input))
    println(Day15.partTwo(input))
}