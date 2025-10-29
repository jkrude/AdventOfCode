package y2024

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class Day6Test {

    val testData = """
    ....#.....
    .........#
    ..........
    ..#.......
    .......#..
    ..........
    .#..^.....
    ........#.
    #.........
    ......#...
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(41, y2024.Day6.partOne(testData))
    }
    @Test
    internal fun partTwo() {
        assertEquals(6,y2024.Day6.partTwo(testData))
    }
}