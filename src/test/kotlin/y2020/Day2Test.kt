package y2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day2Test {

    val getTestData = """
    1-3 a: abcde
    1-3 b: cdefg
    2-9 c: ccccccccc
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(2, y2020.Day2.partOne(getTestData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(1, y2020.Day2.partTwo(getTestData))
    }
}