package y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day17Test {

    val testData = """
    2413432311323
    3215453535623
    3255245654254
    3446585845452
    4546657867536
    1438598798454
    4457876987766
    3637877979653
    4654967986887
    4564679986453
    1224686865563
    2546548887735
    4322674655533
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(102, y2023.Day17.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(94, y2023.Day17.partTwo(testData))
    }
}