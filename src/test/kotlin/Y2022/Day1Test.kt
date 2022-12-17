package Y2022

import junit.framework.TestCase

class Day1Test : TestCase() {
    val testData = """
        1000
        2000
        3000

        4000

        5000
        6000

        7000
        8000
        9000

        10000
    """.trimIndent()

    fun `test maxNCalories`() {
        assertEquals(45000, maxNCalories(testData.split("\n"), 3))
        assertEquals(45000, bestN(testData.split("\n"), 3))
    }
}