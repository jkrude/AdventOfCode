package y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day22Test {

    val testData = """
    1
    10
    100
    2024
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(37327623L, y2024.Day22.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        val testDatapartTwo = """
            1
            2
            3
            2024
        """.trimIndent().lines()
        assertEquals(23L, y2024.Day22.partTwo(testDatapartTwo))
    }
}