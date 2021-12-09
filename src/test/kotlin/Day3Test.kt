import junit.framework.TestCase

class Day3Test : TestCase() {

    private val testData = """
        00100
        11110
        10110
        10111
        10101
        01111
        00111
        11100
        10000
        11001
        00010
        01010""".trimIndent().split("\n")

    fun testPartOne() {
        assertEquals(198, Day3.partOne(testData))
    }

    fun testPartTwo() {
        assertEquals(230, Day3.partTwo(testData))
    }
}
