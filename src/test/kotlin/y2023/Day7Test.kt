package y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day7Test {

    val testData = """
    32T3K 765
    T55J5 684
    KK677 28
    KTJJT 220
    QQQJA 483
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(6440L, y2023.Day7.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(5905L, y2023.Day7.partTwo(testData))
    }
}