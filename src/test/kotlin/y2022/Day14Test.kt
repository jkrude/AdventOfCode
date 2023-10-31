package y2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day14Test {

    val testData = """
    498,4 -> 498,6 -> 496,6
    503,4 -> 502,4 -> 502,9 -> 494,9
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        //Day14.visualize(Day14.Companion.GroundMap(testData))
        assertEquals(24, y2022.Day14.partOne(testData))
    }

    @Test
    internal fun partTwo() {
//        assertEquals(93, Y2022.Day14.partTwo(testData))
        assertEquals(93, y2022.Day14.partTwo(testData))
    }
}