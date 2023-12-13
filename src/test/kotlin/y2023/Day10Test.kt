package y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day10Test {

    val testData = """
    ..F7.
    .FJ|.
    SJ.L7
    |F--J
    LJ...
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        val simple = """
            .....
            .S-7.
            .|.|.
            .L-J.
            .....
        """.trimIndent().lines()
        assertEquals(4, y2023.Day10.partOne(simple))
        assertEquals(8, y2023.Day10.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        val simple = """
            ...........
            .S-------7.
            .|F-----7|.
            .||.....||.
            .||.....||.
            .|L-7.F-J|.
            .|..|.|..|.
            .L--J.L--J.
            ...........
        """.trimIndent().lines()
        assertEquals(4, y2023.Day10.partTwo(simple))
        val y = """
            .F----7F7F7F7F-7....
            .|F--7||||||||FJ....
            .||.FJ||||||||L7....
            FJL7L7LJLJ||LJ.L-7..
            L--J.L7...LJS7F-7L7.
            ....F-J..F7FJ|L7L7L7
            ....L7.F7||L7|.L7L7|
            .....|FJLJ|FJ|F7|.LJ
            ....FJL-7.||.||||...
            ....L---J.LJ.LJLJ...
        """.trimIndent().lines()
        assertEquals(8, y2023.Day10.partTwo(y))
    }
}