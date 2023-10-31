package y2021

import Day2
import junit.framework.TestCase

class Day2Test : TestCase() {

    val testData = """
        forward 5
        down 5
        forward 8
        up 3
        down 8
        forward 2""".trimIndent().split("\n").map { it.split(" ") }.map { it[0] to it[1].toInt() }

    fun testpartOne() {
        assertEquals(150, Day2.partOne(testData))
    }

    fun testpartTwo() {
        assertEquals(900, Day2.partTwo(testData))
    }
}