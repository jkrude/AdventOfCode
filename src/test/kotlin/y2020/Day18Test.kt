package y2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day18Test {

    val getTestData = """
    
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(26L, Day18.partOne(listOf("2 * 3 + (4 * 5)")))
        assertEquals(437L, Day18.partOne(listOf("5 + (8 * 3 + 9 + 3 * 4 * 3)")))
        assertEquals(12240L, Day18.partOne(listOf("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")))
        assertEquals(13632L, Day18.partOne(listOf("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")))
    }

    @Test
    internal fun partTwo() {
        assertEquals(51L, Day18.partTwo(listOf("1 + (2 * 3) + (4 * (5 + 6))")))
        assertEquals(46L, Day18.partTwo(listOf("2 * 3 + (4 * 5)")))
        assertEquals(1445L, Day18.partTwo(listOf("5 + (8 * 3 + 9 + 3 * 4 * 3)")))
        assertEquals(669060L, Day18.partTwo(listOf("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")))
        assertEquals(23340L, Day18.partTwo(listOf("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")))
    }
}