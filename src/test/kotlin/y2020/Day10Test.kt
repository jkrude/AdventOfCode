package y2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day10Test {

    val getTestData = """
    16
    10
    15
    5
    1
    11
    7
    19
    6
    12
    4
    """.trimIndent().lines()

    val testDataBig = """
        28
        33
        18
        42
        31
        14
        46
        20
        48
        47
        24
        23
        49
        45
        19
        38
        39
        11
        1
        32
        25
        35
        8
        17
        7
        9
        4
        2
        34
        10
        3
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(7 * 5, y2020.Day10.partOne(getTestData))
        assertEquals(22 * 10, y2020.Day10.partOne(testDataBig))
    }

    @Test
    internal fun partTwo() {
        assertEquals(8, y2020.Day10.partTwo(getTestData))
        assertEquals(19208, y2020.Day10.partTwo(testDataBig))
    }
}