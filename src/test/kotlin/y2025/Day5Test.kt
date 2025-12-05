package y2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day5Test {

    val testData = """
    3-5
    10-14
    16-20
    12-18

    1
    5
    8
    11
    17
    32
    """.trimIndent()

    @Test
    internal fun partOne() {
        //assertEquals(L, y2025.Day5.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(14L, y2025.Day5.partTwo(testData))
    }
}