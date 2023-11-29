package y2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day21Test {

    val testData = """
    root: pppw + sjmn
    dbpl: 5
    cczh: sllz + lgvd
    zczc: 2
    ptdq: humn - dvpt
    dvpt: 3
    lfqf: 4
    humn: 5
    ljgn: 2
    sjmn: drzm * dbpl
    sllz: 4
    pppw: cczh / lfqf
    lgvd: ljgn * ptdq
    drzm: hmdt - zczc
    hmdt: 32
    """.trimIndent().lines()

    @Test
    internal fun partOne() {
        assertEquals(152, y2022.Day21.partOne(testData))
    }

    @Test
    internal fun partTwo() {
        assertEquals(301, y2022.Day21.partTwo(testData))
    }
}