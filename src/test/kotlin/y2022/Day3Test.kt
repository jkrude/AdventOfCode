package y2022

import org.junit.jupiter.api.Assertions.assertEquals

internal class Day3Test {

    val testData = """
        vJrwpWtwJgWrhcsFMMfFFhFp
        jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
        PmmdzqPrVvPwwTWBwg
        wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
        ttgJtRGJQctTZtZT
        CrZsJsPPZsGzwwsLwLmpwMDw""".trimIndent().lines()

    @org.junit.jupiter.api.Test
    internal fun partOne() {
        assertEquals(157, y2022.partOneBrute(testData))
    }

    @org.junit.jupiter.api.Test
    internal fun partTwo() {
        assertEquals(70, y2022.partTwo(testData))
    }
}

