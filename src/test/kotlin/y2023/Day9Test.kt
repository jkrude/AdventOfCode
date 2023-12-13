package y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day9Test {

    val testData = """
    0 3 6 9 12 15
    1 3 6 10 15 21
    10 13 16 21 30 45
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(114L, y2023.Day9.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(2L, y2023.Day9.partTwo(testData))
    }
}