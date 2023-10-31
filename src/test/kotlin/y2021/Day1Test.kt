package y2021

import org.junit.jupiter.api.Assertions.assertEquals

class Day1Test {

    private val testData: List<String> = """
        199
        200
        208
        210
        200
        207
        240
        269
        260
        263""".trimIndent().split("\n")

    fun testSampleData() {
        val input = Day1.readReport(testData)
        assertEquals(7, Day1.partOne(input))
        assertEquals(5, Day1.partTwo(input))
    }

    fun testRealData() {
        val input = Day1.readReport()
        assertEquals(1342, Day1.partOne(input))
        assertEquals(1378, Day1.partTwo(input))
    }
}