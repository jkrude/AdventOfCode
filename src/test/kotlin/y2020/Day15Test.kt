package y2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day15Test {

    val getTestData = """
    0,3,6
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(436, y2020.Day15.partOne(getTestData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(175594, y2020.Day15.partTwo(getTestData))
    }
}