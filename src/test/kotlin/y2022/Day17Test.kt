package y2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day17Test {

    val testData = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"

    @Test
    internal fun partOne() {
        assertEquals(3068, y2022.Day17.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        //assertEquals(,Y2022.Day17.partTwo(testData))
    }
}