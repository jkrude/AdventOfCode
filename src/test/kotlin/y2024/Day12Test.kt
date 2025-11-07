package y2024

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class Day12Test {

    val testData = """
    RRRRIICCFF
    RRRRIICCCF
    VVRRRCCFFF
    VVRCCCJFFF
    VVVVCJJCFE
    VVIVCCJJEE
    VVIIICJJEE
    MIIIIIJJEE
    MIIISIJEEE
    MMMISSJEEE
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(1930, y2024.Day12.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        val E = """
            EEEEE
            EXXXX
            EEEEE
            EXXXX
            EEEEE
        """.trimIndent().lines()
        assertEquals(236, y2024.Day12.partTwo(E))

        val As = """
            AAAAAA
            AAABBA
            AAABBA
            ABBAAA
            ABBAAA
            AAAAAA
        """.trimIndent().lines()
        assertEquals(368, y2024.Day12.partTwo(As))

        assertEquals(1206, y2024.Day12.partTwo(testData))
    }
}