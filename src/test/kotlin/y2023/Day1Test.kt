package y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day1Test {

    val testData = """
    two1nine
    eightwothree
    abcone2threexyz
    xtwone3four
    4nineeightseven2
    zoneight234
    7pqrstsixteen
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        //assertEquals(L, y2023.Day1.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(281, y2023.Day1.partTwo(testData))
    }
}