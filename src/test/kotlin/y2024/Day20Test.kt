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
        //assertEquals(L, y2024.Day20.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(3L, y2024.Day20.partTwo(testData, 30))
    }
}