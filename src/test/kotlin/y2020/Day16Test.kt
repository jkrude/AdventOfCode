package y2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day16Test {

    val getTestData = """
    class: 1-3 or 5-7
    row: 6-11 or 33-44
    seat: 13-40 or 45-50
    
    your ticket:
    7,1,14
    
    nearby tickets:
    7,3,47
    40,4,50
    55,2,20
    38,6,12
    """.trimIndent()

    @Test
    internal fun partOne() {
        assertEquals(71, y2020.Day16.partOne(getTestData))
    }

    @Test
    internal fun partTwo() {
        val (rules, _, others) = Day16.parse(getTestData)
        val valid = others.filter { t -> t.all { rules.any { r -> r.isValid(it) } } }
        assertEquals(listOf(1, 0, 2), Day16.findRuleAssignments(rules, valid))
    }
}