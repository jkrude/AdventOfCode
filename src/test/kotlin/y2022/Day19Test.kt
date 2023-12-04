package y2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day19Test {

    val testData = """
    Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
    Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(33, y2022.Day19.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(56L * 62L, Day19.partTwo(testData))
    }
}