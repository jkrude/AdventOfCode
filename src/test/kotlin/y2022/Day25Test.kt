package y2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day25Test {

    val testData = """
    1=-0-2
    12111
    2=0=
    21
    2=01
    111
    20012
    112
    1=-1=
    1-12
    12
    1=
    122
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals("2=-1=0", y2022.Day25.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        //assertEquals(,Y2022.Day25.partTwo(testData))
    }
}