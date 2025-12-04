package y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day21Test {

    val testData = """
    029A
    980A
    179A
    456A
    379A
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(126384L, Day21.partOne(testData))
    }
    // There is no example solution for part two.
}