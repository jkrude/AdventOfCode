package y2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day14Test {

    val getTestData = """
    mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
    mem[8] = 11
    mem[7] = 101
    mem[8] = 0
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(165uL, y2020.Day14.partOne(getTestData))
    }

    @Test
    internal fun partTwo() {
        val testData = """
            mask = 000000000000000000000000000000X1001X
            mem[42] = 100
            mask = 00000000000000000000000000000000X0XX
            mem[26] = 1
        """.trimIndent().lines()
        assertEquals(208uL, y2020.Day14.partTwo(testData))
    }
}