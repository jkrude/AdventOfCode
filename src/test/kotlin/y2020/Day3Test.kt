package y2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day3Test {

    val getTestData = """
    ..##.......
    #...#...#..
    .#....#..#.
    ..#.#...#.#
    .#...##..#.
    ..#.##.....
    .#.#.#....#
    .#........#
    #.##...#...
    #...##....#
    .#..#...#.#
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(7, y2020.Day3.partOne(getTestData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(336L, y2020.Day3.partTwo(getTestData))
    }
}