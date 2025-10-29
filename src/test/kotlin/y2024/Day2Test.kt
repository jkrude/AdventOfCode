package y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day2Test {

    val testData = """
    7 6 4 2 1
    1 2 7 8 9
    9 7 6 2 1
    1 3 2 4 5
    8 6 4 4 1
    1 3 6 7 9
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(2, y2024.Day2.partOne(testData))
    }
    @Test
    internal fun partTwo() {
        assertEquals(4,y2024.Day2.partTwo(testData))
    }
}