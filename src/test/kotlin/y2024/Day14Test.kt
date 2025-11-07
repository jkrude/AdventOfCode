package y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day14Test {

    val testData = """
    p=0,4 v=3,-3
    p=6,3 v=-1,-3
    p=10,3 v=-1,2
    p=2,0 v=2,-1
    p=0,0 v=1,3
    p=3,0 v=-2,-2
    p=7,6 v=-1,-3
    p=3,0 v=-1,-2
    p=9,3 v=2,3
    p=7,3 v=-1,2
    p=2,4 v=2,-3
    p=9,5 v=-3,-3
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(12L, y2024.Day14.partOne(testData, xMax = 11, yMax = 7))
    }
    @Test
    internal fun partTwo() {
        //assertEquals(L,y2024.Day14.partTwo(testData))
    }
}