package y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day20Test {

    val testData = """
    broadcaster -> a, b, c
    %a -> b
    %b -> c
    %c -> inv
    &inv -> a
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(32000000L, y2023.Day20.partOne(testData))
    }

    // There are no test cases for part two.
}