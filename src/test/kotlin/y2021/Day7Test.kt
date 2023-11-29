package y2021

import org.junit.jupiter.api.Assertions.assertEquals

class Day7Test {

    private val testData = listOf(16, 1, 2, 0, 4, 2, 7, 1, 2, 14)


    fun testPartOne() {
        assertEquals(37, Day7.partOne(testData))
    }

    fun testPartTwo() {
        assertEquals(168, Day7.partTwo(testData))
    }
}