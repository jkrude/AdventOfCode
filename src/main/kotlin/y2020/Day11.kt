package y2020

import common.extensions.*
import common.extensions.Lists2D.getOrNull
import common.extensions.Lists2D.iterateUntilNoChange
import common.extensions.Lists2D.map2DIndexed
import common.readFileLines

object Day11 {

    private fun genericNeighboursEmpty(neighbours: List<Char>): Boolean =
        neighbours.all { it == 'L' || it == '.' }

    private fun nOccupied(n: Int, neighbours: List<Char>) =
        neighbours.count { it == '#' } >= n


    private fun directNeighboursEmpty(seats: List2D<Char>, ij: Pair<Int, Int>): Boolean =
        genericNeighboursEmpty(
            ij.allNeighbour().mapNotNull { seats.getOrNull(it) }
        )


    private fun fourOccupied(seats: List2D<Char>, ij: Pair<Int, Int>): Boolean =
        nOccupied(4, ij.allNeighbour().mapNotNull { seats.getOrNull(it) })


    private fun List2D<Char>.applyRound(
        neighboursEmpty: (List2D<Char>, ij: Pair<Int, Int>) -> Boolean,
        occupied: (List2D<Char>, ij: Pair<Int, Int>) -> Boolean,
    ): Pair<Boolean, List<List<Char>>> {
        var changed = false
        val next = this.map2DIndexed { ij, place ->
            when {
                place == 'L' && neighboursEmpty(this, ij) -> '#'.also { changed = true }
                place == '#' && occupied(this, ij) -> 'L'.also { changed = true }
                else -> place
            }
        }
        return changed to next
    }


    fun partOne(lines: List<String>): Int {
        return iterateUntilNoChange(lines.toCharList2D()) { lastSeats ->
            lastSeats.applyRound(
                ::directNeighboursEmpty,
                ::fourOccupied
            )
        }
            .flatten().count { it == '#' }
    }

    private fun List<List<Char>>.firstVisibleOrNull(direction: Pair<Int, Int>, start: Pair<Int, Int>): Char? {
        var i = 1
        do {
            val ele = this.getOrNull(start + direction.map { it * i }) ?: return null
            if (ele != '.') return ele
            i += 1
        } while (true)
    }

    private fun List<List<Char>>.visibleNeighbours(ij: Pair<Int, Int>): List<Char> {
        val directions = listOf(
            (1 to 0 + 1),
            (1 to 0),
            (1 to 0 - 1),
            (0 to 0 + 1),
            (0 to 0 - 1),
            (-1 to 0 - 1),
            (-1 to 0),
            (-1 to 0 + 1)
        )
        return directions.mapNotNull { this.firstVisibleOrNull(it, ij) }
    }

    private fun visibleNeighboursEmpty(seats: List2D<Char>, ij: Pair<Int, Int>): Boolean =
        genericNeighboursEmpty(seats.visibleNeighbours(ij))


    private fun fiveOccupied(seats: List2D<Char>, ij: Pair<Int, Int>): Boolean =
        nOccupied(5, seats.visibleNeighbours(ij))


    fun partTwo(lines: List<String>): Int {
        return iterateUntilNoChange(lines.toCharList2D()) { lastSeats ->
            lastSeats.applyRound(
                ::visibleNeighboursEmpty,
                ::fiveOccupied
            )
        }
            .flatten()
            .count { it == '#' }
    }
}

fun main() {
    println(Day11.partOne(readFileLines(11, 2020)))
    println(Day11.partTwo(readFileLines(11, 2020)))
}