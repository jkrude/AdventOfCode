package y2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day6Test {

    val testData = """
    123 328  51 64 
     45 64  387 23 
      6 98  215 314
    *   +   *   +  
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        //assertEquals(L, y2025.Day6.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(3263827L, y2025.Day6.partTwo(testData))
    }
}