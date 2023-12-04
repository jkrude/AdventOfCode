package y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day3Test {

    val testData = """
    467..114..
    ...*......
    ..35..633.
    ......#...
    617*......
    .....+.58.
    ..592.....
    ......755.
    ...$.*....
    .664.598..
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(4361, y2023.Day3.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(467835L, y2023.Day3.partTwo(testData))
    }
}