import junit.framework.TestCase

class Day1Test : TestCase() {

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
        assertEquals(7, Day1().partOne(testData))
        assertEquals(5, Day1().partTwo(testData))
    }

    fun testRealData() {
        val data = readReport(1)
        assertNotNull(data)
        assertEquals(1342, Day1().partOne(data!!))
        assertEquals(1378, Day1().partTwo(data))
    }
}