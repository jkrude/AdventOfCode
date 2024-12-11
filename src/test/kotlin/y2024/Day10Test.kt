package y2024

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class Day10Test {

    val testData = """
    89010123
    78121874
    87430965
    96549874
    45678903
    32019012
    01329801
    10456732
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(36, y2024.Day10.partOne(testData))
    }
    @Test
    internal fun partTwo() {
        assertEquals(81,y2024.Day10.partTwo(testData))
    }
}