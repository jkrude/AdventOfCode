package y2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day3Test {

    val testData = """
    987654321111111
    811111111111119
    234234234234278
    818181911112111
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(357L, y2025.Day3.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(3121910778619L, y2025.Day3.partTwo(testData))
    }
}