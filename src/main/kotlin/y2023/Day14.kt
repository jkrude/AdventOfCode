package y2023

import common.Direction
import common.Direction.*
import common.extensions.Lists2D.indices2d
import common.readFileLines

object Day14 {

    private fun northBeanLoad(tilted: List<List<Char>>) =
        tilted.indices2d().sumOf { (i, j) ->
            if (tilted[i][j] == 'O') tilted.size - i else 0
        }

    private fun List<List<Char>>.get(first: Int, second: Int, direction: Direction) =
        if (direction.isVertical()) this[first][second] else this[second][first]

    private fun List<MutableList<Char>>.set(first: Int, second: Int, value: Char, direction: Direction) =
        if (direction.isVertical()) this[first][second] = value else this[second][first] = value

    private fun MutableList<MutableList<Char>>.rollUntil(outerIdx: Int, innerIdx: Int, direction: Direction) {
        var variable = outerIdx
        val delta = if (direction == NORTH || direction == EAST) -1 else 1
        val allowedIndex = if (delta == -1) (1..this.lastIndex) else (0..<this.lastIndex)
        var changed = false
        // Go through the row (column) to find the point to which the ball will roll.
        // If direction is vertical go through a column else through the row
        while ((variable in allowedIndex) && get(variable + delta, innerIdx, direction) == '.') {
            variable += delta
            changed = true
        }
        set(variable, innerIdx, 'O', direction)
        if (changed) set(outerIdx, innerIdx, '.', direction)
    }


    private fun roll(tilted: MutableList<MutableList<Char>>, direction: Direction) {
        require(tilted.size == tilted.first().size, { "Assumed square with width = height" })
        // We have to start from the point to which the balls will roll
        val outerIndices = if (direction != SOUTH && direction != WEST) tilted.indices else tilted.indices.reversed()
        for (outerIdx in outerIndices) {
            for (innerIdx in tilted.indices) {
                if (tilted.get(outerIdx, innerIdx, direction) == 'O') {
                    tilted.rollUntil(outerIdx, innerIdx, direction)
                }
            }
        }
    }

    fun partOne(lines: List<String>): Int {
        val tilted = lines
            .map { it.toMutableList() }
            .toMutableList()
        roll(tilted, NORTH)
        return northBeanLoad(tilted)
    }


    fun partTwo(lines: List<String>): Int {
        val tilted = lines
            .map { it.toMutableList() }
            .toMutableList()

        fun update() = listOf(NORTH, EAST, SOUTH, WEST).forEach { roll(tilted, it) }

        // Hashcode to turn
        // NOTE the hash is not guaranteed to be collision free but is good enough for the provided instances
        val seen = mutableMapOf<Int, Int>()

        var i = 1
        while (true) {
            update()
            val hash = tilted.hashCode()
            if (hash in seen) break
            seen[hash] = i
            i++
        }
        val hash = tilted.hashCode()
        val loopLength = i - seen[hash]!!
        val repeat = (1000000000L - i) / loopLength
        val leftOver = (1000000000L - i) - repeat * loopLength
        repeat(leftOver.toInt()) { update() }
        return northBeanLoad(tilted)
    }
}

fun main() {
    val input = readFileLines(14, 2023)
    println(Day14.partOne(input))
    println(Day14.partTwo(input))
}