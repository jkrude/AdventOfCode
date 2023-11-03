package y2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day8Test {

    val getTestData = """
    nop +0
    acc +1
    jmp +4
    acc +3
    jmp -3
    acc -99
    acc +1
    jmp -4
    acc +6
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(5, y2020.Day8.partOne(getTestData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(8, y2020.Day8.partTwo(getTestData))
    }
}