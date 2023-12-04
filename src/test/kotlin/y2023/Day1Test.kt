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
        val partOneTest = """
         1abc2
         pqr3stu8vwx
         a1b2c3d4e5f
         treb7uchet
        """.trimIndent().lines()
        assertEquals(142, y2023.Day1.partOne(partOneTest))
    }

    @Test
    internal fun partTwo() {
        assertEquals(281, y2023.Day1.partTwo(testData))
    }
}