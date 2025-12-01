package y2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day1Test {

    val testData = """
    L68
    L30
    R48
    L5
    R60
    L55
    L1
    L99
    R14
    L82
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(3L, y2025.Day1.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(6L, y2025.Day1.partTwo(testData))
    }
}