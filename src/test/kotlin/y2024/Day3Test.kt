package y2024

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class Day3Test {

    val testData = """
    xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
    """.trimIndent()

    @Test
    internal fun partOne() {
        assertEquals(161L, y2024.Day3.partOne(testData))
    }
    @Test
    internal fun partTwo() {
        assertEquals(48L, y2024.Day3.partTwo(testData))
    }
}