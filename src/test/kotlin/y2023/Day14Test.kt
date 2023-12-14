package y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day14Test {

    val testData = """
    O....#....
    O.OO#....#
    .....##...
    OO.#O....O
    .O.....O#.
    O.#..O.#.#
    ..O..#O..O
    .......O..
    #....###..
    #OO..#....
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(136, y2023.Day14.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(64, y2023.Day14.partTwo(testData))
    }
}