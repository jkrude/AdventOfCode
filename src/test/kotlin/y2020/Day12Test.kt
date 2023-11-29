package y2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day12Test {

    val getTestData = """
    F10
    N3
    F7
    R90
    F11
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(25, y2020.Day12.partOne(getTestData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(286, y2020.Day12.partTwo(getTestData))
    }
}