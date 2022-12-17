package Y2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day20Test {

    val testData = """
    1
    2
    -3
    3
    -2
    0
    4
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(3, Y2022.Day20.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(1623178306, Y2022.Day20.partTwo(testData))
    }
}