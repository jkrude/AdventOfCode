package Y2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day2Test {

    val testData = """
        A Y
        B X
        C Z
    """.trimIndent().lines()

    @Test
    fun followStrategy() {
        assertEquals(Y2022.followStrategy(testData), 15)
    }

    @Test
    fun findOutStrategy() {
        assertEquals(figureOutStrategy(testData), 12)
    }
}