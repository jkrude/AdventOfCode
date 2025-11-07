package y2024

import common.algorithms.Search
import common.readFileText

object Day19 {

    private fun setup(input: String): Pair<List<String>, List<String>> {
        val (towelsStr, patternStr) = input.split("\n\n")
        return towelsStr.split(", ") to patternStr.lines()
    }


    fun partOne(input: String): Long {
        val (towels, patterns) = setup(input)
        return patterns.count { pattern ->
            Search
                .SearchBuilder(listOf(0))
                .neighbors { idx: Int ->
                    towels.mapNotNull { towel ->
                        val endIdx = idx + towel.length
                        if (endIdx <= pattern.length && pattern.substring(idx, endIdx) == towel) endIdx else null
                    }
                }.stopAt { it == pattern.length }
                .executeDfs() != null
        }.toLong()
    }

    fun partTwo(input: String): Long {
        val (towels, patterns) = setup(input)

        val combinations: MutableMap<String, Long> = mutableMapOf()
        fun countCombinations(input: String): Long {
            if (input.isEmpty()) return 1L
            return combinations[input] ?: towels.sumOf { towel ->
                if (input.startsWith(towel)) countCombinations(input.substring(towel.length))
                else 0
            }.also { combinations[input] = it }
        }
        return patterns.sumOf(::countCombinations)
    }
}

fun main() {
    val input = readFileText(19, 2024)
    println(Day19.partOne(input))
    println(Day19.partTwo(input))
}