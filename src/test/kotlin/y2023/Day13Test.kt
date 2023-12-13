package y2023

import common.extensions.toCharList2D
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day13Test {

    val first = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
        """.trimIndent().lines().toCharList2D()
    val second = """
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
        """.trimIndent().lines().toCharList2D()

    @Test
    internal fun partOne() {
        assertEquals(5, y2023.Day13.verticalSplit(first))
        assertEquals(null, y2023.Day13.horizontalSplit(first))
        assertEquals(4, y2023.Day13.horizontalSplit(second))
        assertEquals(null, y2023.Day13.verticalSplit(second))
    }

    @Test
    internal fun partTwo() {

        assertEquals(3, y2023.Day13.horizontalSplit(first, true))
        assertEquals(null, y2023.Day13.verticalSplit(first, true))

        assertEquals(1, y2023.Day13.horizontalSplit(second, true))
        assertEquals(null, y2023.Day13.verticalSplit(second, true))
    }
}