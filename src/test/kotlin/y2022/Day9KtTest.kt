package y2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day9KtTest {

    val testData = """
    R 4
    U 4
    L 3
    D 1
    R 4
    D 1
    L 5
    R 2
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(13, y2022.Day9.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(1, y2022.Day9.partTwo(testData))
        val largerTest = """
            R 5
            U 8
            L 8
            D 3
            R 17
            D 10
            L 25
            U 20
        """.trimIndent().lines()
        assertEquals(36, y2022.Day9.partTwo(largerTest))
    }
}