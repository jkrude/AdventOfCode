import junit.framework.TestCase

class Day9Test : TestCase() {

    val testData: List<String> = """
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678""".trimIndent().split("\n")

    fun testPartOne() {
        assertEquals(15, Day9.partOne(Day9.readReport(testData)))
    }

    fun testPartTwo() {
        assertEquals(1134, Day9.partTwo(Day9.readReport(testData)))
    }
}