package y2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day9Test {

    val testData = """
    7,1
    11,1
    11,7
    9,7
    9,5
    2,5
    2,3
    7,3
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(50L, y2025.Day9.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(24L, y2025.Day9.partTwo(testData))
    }
}