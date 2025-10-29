package y2024

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class Day9Test {

    val testData = """2333133121414131402""".trimIndent()

    @Test
    internal fun partOne() {
        assertEquals(1928L, y2024.Day9.partOne(testData))
    }
    @Test
    internal fun partTwo() {
        assertEquals(2858L,y2024.Day9.partTwo(testData))
    }
}