package y2024

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class Day16Test {

    val testData = """
    ###############
    #.......#....E#
    #.#.###.#.###.#
    #.....#.#...#.#
    #.###.#####.#.#
    #.#.#.......#.#
    #.#.#####.###.#
    #...........#.#
    ###.#.#####.#.#
    #...#.....#.#.#
    #.#.#.###.#.#.#
    #.....#...#.#.#
    #.###.#.#.#.#.#
    #S..#.....#...#
    ###############
    """.trimIndent()

    @Test
    internal fun partOne() {
        assertEquals(7036L, y2024.Day16.partOne(testData))
    }
    @Test
    internal fun partTwo() {
        assertEquals(45L,y2024.Day16.partTwo(testData))
    }
}