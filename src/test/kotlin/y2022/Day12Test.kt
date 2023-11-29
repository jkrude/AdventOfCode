package y2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day12Test {

    val testData = """
    Sabqponm
    abcryxxl
    accszExk
    acctuvwj
    abdefghi
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(31, y2022.Day12.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(29, y2022.Day12.partTwo(testData))
    }
}