package y2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day1Test {

    val getTestData = """
    1721
    979
    366
    299
    675
    1456
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(514579, y2020.Day1.partOne(getTestData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(241861950, y2020.Day1.partTwo(getTestData))
    }
}