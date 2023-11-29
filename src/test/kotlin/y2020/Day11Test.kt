package y2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day11Test {

    val getTestData = """
    L.LL.LL.LL
    LLLLLLL.LL
    L.L.L..L..
    LLLL.LL.LL
    L.LL.LL.LL
    L.LLLLL.LL
    ..L.L.....
    LLLLLLLLLL
    L.LLLLLL.L
    L.LLLLL.LL
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(37, y2020.Day11.partOne(getTestData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(26, y2020.Day11.partTwo(getTestData))
    }
}