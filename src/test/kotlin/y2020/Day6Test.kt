package y2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day6Test {

    val getTestData = """
    abc

    a
    b
    c

    ab
    ac

    a
    a
    a
    a

    b
    """.trimIndent().split("\n\n")

    @Test
    internal fun partOne() {
        assertEquals(11, y2020.Day6.partOne(getTestData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(6, y2020.Day6.partTwo(getTestData))
    }
}