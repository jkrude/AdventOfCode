package y2024

import common.extensions.takePair
import common.findAllLong
import common.readFileText

object Day5 {

    private fun parse(input: String): Pair<Map<Long, List<Long>>, List<List<Long>>> {
        val (orderString, updateString) = input.split("\n\n")

        val beforeRelation: Map<Long, List<Long>> = orderString.lines()
            .map { it.findAllLong().takePair() }
            .groupBy { it.second }
            .mapValues { (_, values) -> values.map { it.first } }
            .withDefault { emptyList() }

        val updates: List<List<Long>> = updateString.lines().map { it.findAllLong().toList() }
        return beforeRelation to updates
    }

    private fun sequenceFollowsRules(
        sequence: List<Long>,
        beforeRelation: Map<Long, List<Long>>
    ) = sequence.dropLast(1).withIndex().all { (index, x: Long) ->
        beforeRelation.getValue(x).all { before ->
            sequence.subList(index + 1, sequence.size)
                .none { it == before }
        }
    }


    fun partOne(input: String): Long {

        val (beforeRelation, updates) = parse(input)

        val validUpdates = updates.filter {
            sequenceFollowsRules(it, beforeRelation)
        }
        return validUpdates.sumOf { sequence ->
            sequence[sequence.size / 2]
        }

    }

    fun partTwo(input: String): Long {
        val (beforeRelation, updates) = parse(input)

        val invalid = updates.filterNot { sequence ->
            sequenceFollowsRules(sequence, beforeRelation)
        }
        // -1 = o1 < o2 -> o1 comes before o2 -> o1 in beforeRelation[o2]
        val comp = Comparator<Long> { o1, o2 ->
            when {
                o1 in beforeRelation.getValue(o2) -> -1
                o2 in beforeRelation.getValue(o1) -> 1
                else -> 0
            }
        }
        return invalid.sumOf { sequence ->
            sequence.sortedWith(comp)[sequence.size / 2]
        }
    }
}

fun main() {
    val input = readFileText(5, 2024)
    println(Day5.partOne(input))
    println(Day5.partTwo(input))
}