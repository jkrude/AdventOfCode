package y2022

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day24Test {

    val testData = """
    #.######
    #>>.<^<#
    #.<..<<#
    #>v.><>#
    #<^v^^>#
    ######.#
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(18, y2022.Day24.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(54, y2022.Day24.partTwo(testData))
    }
}