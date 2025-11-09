package y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day20Test {

    val testData = """
    ###############
    #...#...#.....#
    #.#.#.#.#.###.#
    #S#...#.#.#...#
    #######.#.#.###
    #######.#.#...#
    #######.#.###.#
    ###..E#...#...#
    ###.#######.###
    #...###...#...#
    #.#####.#.###.#
    #.#...#.#.#...#
    #.#.#.#.#.#.###
    #...#...#...###
    ###############
    """.trimIndent()

    @Test
    internal fun partOne() {
        assertEquals(44L, y2024.Day20.partOne(testData, 2))
    }

    @Test
    internal fun partTwo() {
        assertEquals(285, y2024.Day20.partTwo(testData, 50))
    }
}