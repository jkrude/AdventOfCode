package y2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day9Test {

    val getTestData = """
    35
    20
    15
    25
    47
    40
    62
    55
    65
    95
    102
    117
    150
    182
    127
    219
    299
    277
    309
    576
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(127L, y2020.Day9.partOne(getTestData, 5))
    }

    @Test
    internal fun partTwo() {
        assertEquals(62, y2020.Day9.partTwo(getTestData, 5))
    }
}