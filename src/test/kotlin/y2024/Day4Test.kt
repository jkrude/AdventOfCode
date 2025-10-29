package y2024

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class Day4Test {

    val testData = """
    MMMSXXMASM
    MSAMXMSMSA
    AMXSXMAAMM
    MSAMASMSMX
    XMASAMXAMM
    XXAMMXXAMA
    SMSMSASXSS
    SAXAMASAAA
    MAMMMXMMMM
    MXMXAXMASX
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(18, y2024.Day4.partOne(testData))
    }
    @Test
    internal fun partTwo() {
        assertEquals(9,y2024.Day4.partTwo(testData))
    }
}