import junit.framework.TestCase

class Day6Test : TestCase() {

    private val testData: List<Int> = listOf(3, 4, 3, 1, 2)

    fun testPartOne() {
        assertEquals(5934, Day6.partOne(testData))
    }

    fun testpartTwo() {
        assertEquals(26984457539, Day6.partTwo(testData))
    }
}