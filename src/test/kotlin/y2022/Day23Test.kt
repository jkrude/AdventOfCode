package y2022

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day23Test {

    val testData = """
    ....#..
    ..###.#
    #...#.#
    .#...##
    #.###..
    ##.#.##
    .#..#..
    """.trimIndent().lines()

    val minimalTestData = """
        .....
        ..##.
        ..#..
        .....
        ..##.
        .....
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(110, y2022.Day23.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(20, y2022.Day23.partTwo(testData))
    }
}