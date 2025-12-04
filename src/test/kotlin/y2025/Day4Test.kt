package y2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day4Test {

    val testData = """
    ..@@.@@@@.
    @@@.@.@.@@
    @@@@@.@.@@
    @.@@@@..@.
    @@.@@@@.@@
    .@@@@@@@.@
    .@.@.@.@@@
    @.@@@.@@@@
    .@@@@@@@@.
    @.@.@@@.@.
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(13L, y2025.Day4.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(43L, y2025.Day4.partTwo(testData))
    }
}