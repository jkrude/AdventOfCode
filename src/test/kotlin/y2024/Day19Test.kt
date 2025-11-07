package y2024

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class Day19Test {

    val testData = """
    r, wr, b, g, bwu, rb, gb, br

    brwrr
    bggr
    gbbr
    rrbgbr
    ubwu
    bwurrg
    brgr
    bbrgwb
    """.trimIndent()

    @Test
    internal fun partOne() {
        assertEquals(6L, y2024.Day19.partOne(testData))
    }
    @Test
    internal fun partTwo() {
        assertEquals(16L,y2024.Day19.partTwo(testData))
    }
}