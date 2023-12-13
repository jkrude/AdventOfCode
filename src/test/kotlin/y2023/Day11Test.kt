package y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day11Test {

    val testData = """
    ...#......
    .......#..
    #.........
    ..........
    ......#...
    .#........
    .........#
    ..........
    .......#..
    #...#.....
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(374, y2023.Day11.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(8410, y2023.Day11.solve(testData, 100 - 1))
    }
}