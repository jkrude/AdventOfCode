package y2022

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day18Test {

    val testData = """
    2,2,2
    1,2,2
    3,2,2
    2,1,2
    2,3,2
    2,2,1
    2,2,3
    2,2,4
    2,2,6
    1,2,5
    3,2,5
    2,1,5
    2,3,5
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(64, y2022.Day18.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(58, y2022.Day18.partTwo(testData))
    }
}