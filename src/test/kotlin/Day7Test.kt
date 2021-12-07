import junit.framework.TestCase
class Day7Test : TestCase() {

    val testData  = listOf(16,1,2,0,4,2,7,1,2,14)


    fun testpartOne() {
        assertEquals(37, Day7.partOne(testData))
    }

    fun testpartTwo() {
        assertEquals(168, Day7.partTwo(testData))
    }
}