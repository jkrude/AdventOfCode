package Y2022

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
        assertEquals(157, Y2022.partOne(testData))
    }

    @org.junit.jupiter.api.Test
    internal fun partTwo() {
        assertEquals(70, Y2022.partTwo(testData))
    }
}

