package y2022

import org.junit.jupiter.api.Assertions.assertEquals

class Day1Test {
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