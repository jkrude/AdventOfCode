package y2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day13Test {

    val getTestData = """
    939
    7,13,x,x,59,x,31,19
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(295, y2020.Day13.partOne(getTestData))
    }

    @Test
    internal fun partTwo() {
        val partTwoTests = listOf(
            "\n5,7" to 20L,
            "\n17,x,13,19" to 3417L,
            "\n67,7,59,61" to 754018L,
            "\n67,x,7,59,61" to 779210L,
            "\n67,7,x,59,61" to 1261476L,
            "\n1789,37,47,1889" to 1202161486L,
        )
        partTwoTests.forEach { (testInput, solution) ->
            assertEquals(solution, y2020.Day13.partTwoZ3(testInput.lines()))
        }

    }
}