package y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day6Test {

    val testData = """
    Time:      7  15   30
    Distance:  9  40  200
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(288, y2023.Day6.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(71503, y2023.Day6.partTwo(testData))
    }
}