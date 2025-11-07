package y2024

import common.algorithms.Search
import common.extensions.Idx2D
import common.extensions.List2D
import common.extensions.Lists2D.get
import common.extensions.Lists2D.getOrNull
import common.extensions.Lists2D.indices2d
import common.extensions.manhattenTo
import common.extensions.neighbours
import common.readFileText
import java.util.Objects.hashCode

object Day20 {

    private data class IdxWithDistance(val idx: Idx2D, val distance: Int) {
        override fun equals(other: Any?): Boolean {
            return other is IdxWithDistance && other.idx == this.idx
        }

        override fun hashCode(): Int {
            return hashCode(this.idx)
        }
    }

    private data class RaceTrack(val track: List2D<Char>, val distanceToEnd: Map<Idx2D, Int>)

    val validSet = setOf('.', 'S', 'E')

    private fun buildRaceTrackWithDistances(input: String): RaceTrack {
        val raceTrack: List2D<Char> = input.lines().map { it.toList() }
        val endPoint = raceTrack.indices2d().find { raceTrack[it] == 'E' }!!
        val distanceToEnd: MutableMap<Idx2D, Int> = mutableMapOf(endPoint to 0)


        // Assign each cell the distance to the goal
        Search.SearchBuilder(listOf(IdxWithDistance(endPoint, 0)))
            .neighbors { (idx2d, distance) ->
                idx2d.neighbours()
                    .filter { n -> raceTrack.getOrNull(n) in validSet }
                    .map { n -> IdxWithDistance(n, (distance + 1)) }
            }
            .onEachVisit { (idx2d, distance) -> distanceToEnd[idx2d] = distance }
            .executeDfs()
        return RaceTrack(raceTrack, distanceToEnd)
    }

    fun partOne(input: String, minDistanceGain: Int = 100): Long {
        val (raceTrack, distanceToEnd) = buildRaceTrackWithDistances(input)
        // Go over each cell and check if any two steps lead to a neighbor that is a good cheat.
        return distanceToEnd.entries.sumOf { (idx2d, distance) ->
            idx2d.neighbours(2)
                .filter { n -> raceTrack.getOrNull(n) in validSet }
                .count { n -> distance - (distanceToEnd[n]!!) > minDistanceGain }
        }.toLong()
    }

    fun partTwo(input: String, minDistanceGain: Int = 100): Long {
        val (_, distanceToEnd) = buildRaceTrackWithDistances(input)
        val cheats = mutableSetOf<Pair<Idx2D, Idx2D>>()
        // O^2 solution testing if any other place is reachable in 20 steps
        // Depending on N and the number of steps it can be faster to go over
        // the neighborhood of currIdx2d instead of testing against known reachable points.
        for ((currIdx2d, distance) in distanceToEnd) {
            for ((otherIdx2d, otherDistance) in distanceToEnd) {
                val steps = (currIdx2d manhattenTo otherIdx2d)
                if (steps <= 20 && distance - (otherDistance + steps) >= minDistanceGain) {
                    cheats.add(currIdx2d to otherIdx2d)
                }
            }
        }
        return cheats.size.toLong()
    }
}

fun main() {
    val input = readFileText(20, 2024)
    println(Day20.partOne(input))
    println(Day20.partTwo(input))
}