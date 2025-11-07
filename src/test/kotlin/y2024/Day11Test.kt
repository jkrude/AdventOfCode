package y2024

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class Day11Test {

    val testData = """125 17""".trimIndent()

    @Test
    internal fun partOne() {
        assertEquals(55312, y2024.Day11.partOne(testData))
    }
}